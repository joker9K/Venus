package data_structures;

/**
 * Created by zhangwt n 2017/11/17.
 */
public class SequenceImpl implements  Sequence{

    private static  ThreadLocalTest<Integer> numberContainer = new ThreadLocalTest<>();

    @Override
    public int getName() {
        numberContainer.set(numberContainer.get()+1);
        return numberContainer.get();
    }

    public static void main(String[] args) {
        Sequence sequence = new SequenceImpl();
        ClientThread thread1 = new ClientThread(sequence);
        ClientThread thread2 = new ClientThread(sequence);
        ClientThread thread3 = new ClientThread(sequence);

        thread1.start();
        thread2.start();
        thread3.start();
    }
}
