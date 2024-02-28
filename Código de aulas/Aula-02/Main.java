import java.io.*;

public class Main {
    public static void main(String[] args) {
        File f = new File("dados/livros.db");
        f.delete();

        Arquivo<Livro> arqLivros;
        Livro l1 = new Livro(-1, "9788563560278", "Odisseia", 15.99F);
        Livro l2 = new Livro(-1, "9788584290482", "Ensino Híbrido", 39.90F);
        Livro l3 = new Livro();
        int id1, id2;

        try {
            arqLivros = new Arquivo<>("dados/livros.db", Livro.class.getConstructor());

            id1 = arqLivros.create(l1);
            System.out.println("Livro criado com o ID: " + id1);

            id2 = arqLivros.create(l2);
            System.out.println("Livro criado com o ID: " + id2);

            if (arqLivros.delete(id2))
                System.out.println("Livro de ID " + id2 + " excluido!");
            else
                System.out.println("Livro de ID " + id2 + " não encontrado!");

            l1.setTitulo("A Odisséia");
            if (arqLivros.update(l1))
                System.out.println("Livro de ID " + l1.getID() + " alterado!");
            else
                System.out.println("Livro de ID " + l1.getID() + " não encontrado!");

            if ((l3 = arqLivros.read(id1)) != null)
                System.out.println(l3);
            else
                System.out.println("Livro de ID " + id1 + " não encontrado!");

            if ((l3 = arqLivros.read(id2)) != null)
                System.out.println(l3);
            else
                System.out.println("Livro de ID " + id2 + " não encontrado!");

            arqLivros.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
