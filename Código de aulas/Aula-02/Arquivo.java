import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;

// Generic class to manage records type T in a file
public class Arquivo<T extends Registro> {

    private static int TAM_CABECALHO = 4; // Header size in bytes
    RandomAccessFile arq;
    String nomeArquivo = "";
    Constructor<T> construtor; // Constructor for creating objects of type T

    // Constructor to create an instance of Arquivo
    public Arquivo(String na, Constructor<T> c) throws Exception {
        this.nomeArquivo = na;
        this.construtor = c;
        arq = new RandomAccessFile(na, "rw");
        if (arq.length() < TAM_CABECALHO) { // se o arquivo está vazio (não tem cabeçalho)
            arq.seek(0); // posiciona no início do arquivo
            arq.writeInt(0); // escreve cabeçalho
        }
    }

    public int create(T obj) throws Exception { // cria um novo registro no arquivo
        arq.seek(0); // posiciona no início do arquivo
        int ultimoID = arq.readInt(); // lê o último ID usado
        ultimoID++; // incrementa o ID
        arq.seek(0); // posiciona no início do arquivo
        arq.writeInt(ultimoID); // atualiza o cabeçalho com o novo ID
        obj.setID(ultimoID); // configura o ID do objeto

        arq.seek(arq.length()); // posiciona no final do arquivo
        // long endereco = arq.getFilePointer();
        byte[] ba = obj.toByteArray(); // obtém o objeto como vetor de bytes
        short tam = (short) ba.length; // obtém o tamanho do vetor de bytes
        arq.write(' '); // escreve o lapide
        arq.writeShort(tam); // escreve o tamanho do registro
        arq.write(ba); // escreve o registro

        return obj.getID(); // retorna o ID do objeto criado
    }

    public T read(int id) throws Exception { // lê um registro do arquivo
        T l = construtor.newInstance(); // cria um novo objeto do tipo T
        byte[] ba; // vetor de bytes
        short tam; // tamanho do registro
        byte lapide; // lapide do registro

        arq.seek(TAM_CABECALHO); // posiciona no início do arquivo
        while (arq.getFilePointer() < arq.length()) { // percorre o arquivo
            lapide = arq.readByte(); // lê a lapide
            tam = arq.readShort(); // lê o tamanho do registro
            if (lapide == ' ') { // se o registro está ativo
                ba = new byte[tam]; // cria um vetor de bytes do tamanho do registro
                arq.read(ba); // lê o registro
                l.fromByteArray(ba); // converte o registro para um objeto
                if (l.getID() == id) // se encontrou o registro
                    return l; // retorna o registro
            } else {
                arq.skipBytes(tam); // se o registro está inativo, pula o registro
            }
        }
        return null;
    }

    public boolean delete(int id) throws Exception { // remove um registro do arquivo
        T l = construtor.newInstance(); // cria um novo objeto do tipo T
        byte[] ba; // vetor de bytes
        short tam;
        byte lapide; // lapide do registro
        long endereco; // posição do registro no arquivo

        arq.seek(TAM_CABECALHO); // posiciona no início do arquivo
        while (arq.getFilePointer() < arq.length()) { // percorre o arquivo
            endereco = arq.getFilePointer(); // obtém a posição do registro
            lapide = arq.readByte(); // lê a lapide
            tam = arq.readShort(); // lê o tamanho do registro
            if (lapide == ' ') { // se o registro está ativo
                ba = new byte[tam]; // cria um vetor de bytes do tamanho do registro
                arq.read(ba); // lê o registro
                l.fromByteArray(ba); // converte o registro para um objeto
                if (l.getID() == id) { // se encontrou o registro
                    arq.seek(endereco); // posiciona no início do registro
                    arq.write('*'); // escreve a lapide
                    return true;
                }
            } else {
                arq.skipBytes(tam);
            }
        }
        return false;

    }

    public boolean update(T objAlterado) throws Exception { // atualiza um registro do arquivo
        T l = construtor.newInstance(); // cria um novo objeto do tipo T
        byte[] ba; // vetor de bytes
        short tam;
        byte lapide;
        long endereco;

        arq.seek(TAM_CABECALHO); // posiciona no início do arquivo
        while (arq.getFilePointer() < arq.length()) { // percorre o arquivo

            endereco = arq.getFilePointer();
            lapide = arq.readByte();
            tam = arq.readShort();
            if (lapide == ' ') {
                ba = new byte[tam];
                arq.read(ba);
                l.fromByteArray(ba); // converte o registro para um objeto
                if (l.getID() == objAlterado.getID()) { // se encontrou o registro
                    byte[] ba2 = objAlterado.toByteArray(); // obtém o objeto como vetor de bytes
                    short tam2 = (short) ba2.length; // obtém o tamanho do vetor de bytes
                    if (tam2 <= tam) { // se o novo registro cabe no espaço do registro antigo
                        arq.seek(endereco + 1 + 2); // posiciona no início do registro
                        arq.write(ba2); // escreve o registro
                    } else {
                        arq.seek(endereco);
                        arq.write('*');
                        arq.seek(arq.length());
                        arq.write(' ');// escreve o lapide
                        arq.writeShort(tam2);
                        arq.write(ba2);
                    }
                    return true;
                }
            } else {
                arq.skipBytes(tam);
            }
        }
        return false;
    }

    public void close() throws Exception {
        arq.close();
    }
}
