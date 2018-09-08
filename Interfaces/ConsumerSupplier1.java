
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ConsumerSupplier1 {

  public static void main(String[] args) {
    // Prints "Hello, World" to the terminal window.
    System.out.println("Hello, World");
    ArrayList<String> sandwichList = new ArrayList<String>();
    Consumer<String> sandwichAdd = s -> sandwichList.add(s);
    Supplier<String> sandwichPrint = () ->sandwichList.toString();
    
    sandwichAdd.accept("A");
    sandwichAdd.accept("B");
    
    System.out.println(sandwichPrint.get());
    
  }

}
