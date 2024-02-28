import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Livro implements Registro {
    private int idLivro;
    private String isbn;
    private String titulo;
    private float preco;

    DecimalFormat df = new DecimalFormat("#,##0.00");

    public Livro() {
        this.idLivro = -1;
        this.titulo = "";
        this.isbn = "";
        this.preco = 0F;
    }

    public Livro(String i, String t, float p) {
        this(-1, i, t, p);
    }

    public Livro(int id, String i, String t, float p) {
        this.idLivro = id;
        this.isbn = i;
        this.titulo = t;
        this.preco = p;
    }

    public int getID() {
        return this.idLivro;
    }

    public void setID(int id) {
        this.idLivro = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public float getPreco() {
        return preco;
    }

    public void setPreco(float preco) {
        this.preco = preco;
    }

    public byte[] toByteArray() throws Exception { // Converte o objeto para um vetor de bytes
        ByteArrayOutputStream ba_out = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(ba_out);
        dos.writeInt(this.idLivro);
        dos.writeUTF(this.isbn);
        dos.writeUTF(this.titulo);
        dos.writeFloat(this.preco);
        return ba_out.toByteArray();
    }

    public void fromByteArray(byte[] ba) throws Exception { // Inicializa o objeto a partir de um vetor de bytes
        ByteArrayInputStream ba_in = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(ba_in);
        this.idLivro = dis.readInt();
        this.isbn = dis.readUTF();
        this.titulo = dis.readUTF();
        this.preco = dis.readFloat();
    }

    public String toString() {
        return "ID: " + this.idLivro +
                "\nISBN: " + this.isbn +
                "\nTítulo: " + this.titulo +
                "\nPreço: " + NumberFormat.getCurrencyInstance().format(this.preco);
    }

}