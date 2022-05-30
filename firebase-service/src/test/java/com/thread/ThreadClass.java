package com.thread;

public class ThreadClass {

    public static void main(String[] arg){
        System.out.println("" );
        Q q = new Q();
        //new Producer(q);
        new consumer(q);

    }
}
    class Producer implements Runnable{
        Q q;

        public void run(){
         int i = 0;
                 while(true){

                     try {
                         Thread.sleep(10000);
                     }
                     catch (Exception e){

                     }
                }
            }
    }

     class Q{
        int num;
        public void put(int num){
            System.out.println("PUT"  );
            this.num = num;
        }

        public void get(){
            System.out.println("GET");
        }

    }
        class consumer implements Runnable{
            Q q;

            public consumer(Q q){
                this.q = q;
                Thread t = new Thread(this,"consumer");
                t.start();
            }

            public consumer() {

            }

            public void run(){
                int i = 0;

                while(true){

                    q.get();
                    try {
                        Thread.sleep(10000);
                    }
                    catch (Exception e){
                    }
                }
            }
        }



