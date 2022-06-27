package DronazonPkg;

public class EndingThread implements Runnable{

    public EndingThread(){ }

    public void start(){ }

    public void run(){
        try {
            System.out.println("---Press a key to stop---");
            System.in.read();
        }catch (Exception e){
            System.out.println(e.toString());
        }
        System.exit(0);
    }

}
