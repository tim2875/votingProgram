package block;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.ArrayList;

public class server {
   static ServerSocket server = null;
   static int port = 5000;
   static Socket socket = null;

   public static void main(String[] args) throws IOException {
      server bc_server = new server();
      server = new ServerSocket(port);
      System.out.println("서버시작");
      ArrayList<blockchain> bc_list = new ArrayList<blockchain>();
      System.out.println("테스트 시작");
      InetAddress ip = InetAddress.getLocalHost();
      System.out.println(ip.getHostAddress());
      
      int count = 0; // 블록체인 갯수 카운트
      while (true) {
         Socket sock = server.accept();
         InetAddress clienthost = sock.getInetAddress();
         int clientport = sock.getPort();
         System.out.println("클라이언트 연결" + clienthost + ":" + clientport);
         ThreadServer thread_client = new ThreadServer(sock, count, bc_list);
         thread_client.start();
         bc_list = thread_client.return_bc_list();
      }
   }
}

class ThreadServer extends Thread {
   private Socket sock;
   int count;
   ArrayList<blockchain> bc_list;
   Scanner input = new Scanner(System.in);

   public ThreadServer(Socket socket, int count, ArrayList<blockchain> bc_list) {
      this.sock = socket;
      this.count = count;
      this.bc_list = bc_list;
   }

   public ArrayList<blockchain> return_bc_list() {
      return bc_list;
   }

   public void run() {
      try {
         BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
         BufferedReader br = new BufferedReader(new InputStreamReader(sock.getInputStream()));

         sendmessage("새로운 투표 생성 = 1, 전체 투표 리스트 출력 = 2, 만들어진 투표 선택 = 3, 안해! = gg ", sock);
         String choose = readmessage(sock);
         System.out.println(choose);
         if (choose.equals("1")) 
         {
            sendmessage("나이(**~**)/제목/시간(************~************) 입력", sock);
            int start_age = Integer.parseInt(readmessage(sock));
            sleep(100);
            int limit_age = Integer.parseInt(readmessage(sock));
            sleep(100);
            System.out.println("시작 나이: " + start_age);
            System.out.println("제한 나이: " + limit_age);
            String title = readmessage(sock);
            sleep(100);
            System.out.println("제목: " + title);

            String start_time = readmessage(sock);
            sleep(100);
            System.out.println("시작 시간 : " + start_time);

            String limit_time = readmessage(sock);
            System.out.println("제한 시간 : " + limit_time);

            // 블록체인생성
            blockchain a1 = new blockchain(start_age, limit_age, title, count, start_time, limit_time);
            bc_list.add(a1);
            int contents_count = 0;

            while (true) 
            {
               sendmessage((contents_count + 1) + "번째 투표 항목을 입력하세요(입력 종료시 gg를!): ", sock);
               String contents = readmessage(sock);
               System.out.println("contends : " + contents);
               if (contents.equals("gg"))
                  break;
               else {
                  bc_list.get(bc_list.size() - 1).contents.add(contents);
                  contents_count++;
               }
            }
            sendmessage("생성완료", sock);
         } 
         else if (choose.equals("2")) 
         {
            sendmessage(Integer.toString(bc_list.size()), sock); 
            if (bc_list.size() != 0) {
               String text = null;
               for (int i = 0; i < bc_list.size(); i++) {
                  if(bc_list.get(i).isTimeRunnable()==1)
                  {
                     sleep(100);
                     //text = (i + 1) + "번째 투표 : " + bc_list.get(i).title;
                     text = bc_list.get(i).title;
                     sendmessage(text, sock);
                     sleep(100);
                     sendmessage(Integer.toString(i), sock);
                     System.out.println("list send!");
                  }
               }
               sleep(100);
               sendmessage("end", sock);
            } else
               sendmessage("만들어진 투표가 없습니다.", sock);
         } 
         else if (choose.equals("3")) 
         {
            sendmessage("몇번째로 만든 투표? 입력 : ",sock);
            int num = Integer.parseInt(readmessage(sock));
            System.out.println("num = "+num);
            if (num > bc_list.size())
            {
               sendmessage("투표없음.\n", sock);
            }
            else
            {
               sendmessage("투표후보출력 = 1, 투표 = 2, 집계 = 5 뭐할까? : ",sock);
               String choice = readmessage(sock);
               System.out.println("choice: "+choice);
               try {
                  if (choice.equals("1")) {
                        sendmessage(bc_list.get(num - 1).title, sock); // 투표제목      
                        System.out.println("title : "+bc_list.get(num - 1).title);
                        sleep(100);
                        sendmessage(Integer.toString(bc_list.get(num - 1).start_age), sock); // 투표나이제한
                        System.out.println("start age : "+Integer.toString(bc_list.get(num - 1).start_age));
                        sleep(100);
                        sendmessage(Integer.toString(bc_list.get(num - 1).limit_age), sock); // 투표나이제한2
                        System.out.println("limit age : "+Integer.toString(bc_list.get(num - 1).limit_age));
                        sleep(100);
                        sendmessage(bc_list.get(num - 1).start_time, sock); // 투표시간제한
                        System.out.println("start time : "+bc_list.get(num - 1).start_time);
                        sleep(100);
                        sendmessage(bc_list.get(num - 1).limit_time, sock); // 투표시간제한2
                        System.out.println("limit time : "+bc_list.get(num - 1).limit_time);
                        sleep(100);
                        sendmessage(Integer.toString(bc_list.get(num-1).contents.size()), sock);
                        for(int i=0;i<bc_list.get(num-1).contents.size();i++)
                        {
                           sleep(100);
                           sendmessage(bc_list.get(num-1).contents.get(i), sock);
                           System.out.println("후보 : "+bc_list.get(num-1).contents.get(i));
                        }
                        sleep(100);                     
                  } else if (choice.equals("2")) {
                     String imei = readmessage(sock);   //imei입력
                     if(!bc_list.get(num - 1).isOverlab(imei))
                        sendmessage("중복", sock);     
                     else
                     {
                        sendmessage("몇번에 투표할래? 입력 : ",sock);
                        String vote = readmessage(sock);
                        System.out.println("vote : "+vote);
                        bc_list.get(num - 1).connect(vote, imei, Integer.toString(num)); // 블록연결
                     }

                  } else if (choice.equals("3")) {
                     bc_list.get(num - 1).blockchain.get(2).data = "변경!"; // 일단
                     // 강제변경
                     sendmessage("변경!",sock);
                  } else if (choice.equals("4")) {
                     bc_list.get(num - 1).isChainValid(bc_list.get(num - 1).blockchain);
                  }
                  else if(choice.equals("5"))
                  {
                     //집계
                     String ans = bc_list.get(num - 1).vote_sum();
                     sendmessage(ans, sock);
                  }
               } catch (Exception e) {
                  System.out.print("Exception, 디버깅 ㄱ\n");
                  e.printStackTrace();
               }
            }
         }else if (choose.equals("4"))
         {
            //종료된 리스트 출력
            sendmessage(Integer.toString(bc_list.size()), sock);
            if (bc_list.size() != 0) {
               String text = null;
               for (int i = 0; i < bc_list.size(); i++) {
                  if(bc_list.get(i).isTimeRunnable()==2)
                  {
                     sleep(150);
                     text = bc_list.get(i).title;
                     sendmessage(text, sock);
                     sleep(150);
                     sendmessage(Integer.toString(i), sock);
                  }
               }
               sleep(100);
               sendmessage("end", sock);
            } else
            {
               sleep(100);
               sendmessage("만들어진 투표가 없습니다.", sock);
            }
         }

         else if (choose.equals("gg")) {
            sendmessage("끝!", sock);
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public void sendmessage(String data, Socket sock) {
      try {
         BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
         //System.out.println("보냄 : " + data);
         bw.write(data + "\r\n");
         bw.flush();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public String readmessage(Socket sock) {
      String message = null;
      try {
         BufferedReader br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
         message = br.readLine();
         //System.out.println("받음: " + message);
      } catch (Exception e) {
         e.printStackTrace();
      }
      return message;
   }
}