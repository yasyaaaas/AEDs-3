public interface Registro { // Interface that defines methods for managing records in a file
    public void setID(int id); // Set the ID of the record

    public int getID(); // Get the ID of the record

    public byte[] toByteArray() throws Exception; // Convert the record to a byte array

    public void fromByteArray(byte[] ba) throws Exception; // Convert a byte array to a record
}
