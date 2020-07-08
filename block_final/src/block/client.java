package block;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class client {
   private Socket sock;
   client(Socket socket)
   {
      this.sock = socket;
   }
   public static String send(Socket sock) {
      String data=null;
      try {
         Scanner input = new Scanner(System.in);
         BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream())); 
         System.out.println("입력 :");
         data = input.nextLine();
         bw.write(data+"\r\n");
         bw.flush();   
      } catch (Exception e) {
         e.printStackTrace();
      }
      return data;
   }
   public static String read(Socket sock) {
      String message = null;
      try {
         BufferedReader br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
         message = br.readLine();
         System.out.println("서버로부터 -> "+message);
      } catch (Exception e) {
         e.printStackTrace();
      }
      return message;
   }
   
public static void main(String[] args) throws IOException {
      int port = 5000;
         Socket sock = new Socket();
         try{
        	 sock.setSoTimeout(5000);
         sock.connect(new InetSocketAddress("192.168.123.131", port), 5000);
         }
         catch(Exception e)
         {
        	 System.out.println("이거다 이거");
        	 e.printStackTrace();
         }
         if(!sock.isConnected())
            System.out.printf("연결안됨");
         else
         {
            System.out.println("서버연결성공");
            try{
            read(sock);
            String text = send(sock);
            if(text.equals("1"))      //블록체인 입력화면
            {
               read(sock);
               //테스트용 메세지, ui대체
               //투표제목, 나이입력
               System.out.println("start_age");
               send(sock);      
               System.out.println("limite_age");
               send(sock);      
               System.out.println("투표제목");
               send(sock);      //투표 제목
               System.out.println("시작시간(YYYY/MM/DD/HH/MM)");
               send(sock);      //시간제한
               System.out.println("제한시간(YYYY/MM/DD/HH/MM)");
               send(sock);      //시간제한
               //----투표항목 입력--
               while(true)
               {
                  read(sock);
                  String contents=send(sock);
                  if (contents.equals("gg"))
                     break;
               }
               read(sock);
            }
            else if(text.equals("2"))
            {
               int num = Integer.parseInt(read(sock));
               if(num!=0)
               {   
                  for(int i=0;i<num;i++)
                  {
                     String message = read(sock);   //투표제목
                     if(message.equals("end"))
                        break;
                     read(sock);   //해당 투표의 index
                  }
               }
               else
                  read(sock);
            }
            else if(text.equals("3"))
            {
               read(sock);
               send(sock);
               String exception = read(sock);
               if (!exception.equals("투표없음."))
               {
                  String choice = send(sock);
                  if (choice.equals("1")) {
                     String msg = read(sock);
                     if(msg.equals("시간오류"))
                        System.out.println("팝업 - 시간 오류메세지");
                     else
                     {
                        String title = msg;
                        String start_age = read(sock);
                        String limit_age = read(sock);
                        String start_time = read(sock);
                        String limite_time = read(sock);
                  
                        String tmp = read(sock);
                        //System.out.println("투표항목↓");
                        for (int i = 0; i < Integer.parseInt(tmp); i++)
                        {
                           //System.out.println(i+1+"바퀴");
                           read(sock);
                        }
                        //System.out.println("리스트끝");
                     }
                  }
                  else if (choice.equals("2")) {
                     send(sock);      //imei 전송
                     String msg = read(sock);
                     if(msg.equals("중복"))
                        System.out.println("팝업 - 중복 오류메세지");
                     else
                     {
                           send(sock);
                     }
                  } else if (choice.equals("3") || choice.equals("4")) {
                     // 필요없음, 테스트용
                  }
                  else if(choice.equals("5"))
                  {
                     read(sock);
                  }
               }
            }
            else if(text.equals("4"))
            {
               int num = Integer.parseInt(read(sock));
               if(num!=0)
               {   
                  for(int i=0;i<num;i++)
                  {
                     String message = read(sock);   //투표제목
                     if(message.equals("end"))
                        break;
                     read(sock);   //해당 투표의 index
                  }
               }
               else
                  read(sock);
            }
            System.out.println("끝!");
            sock.close();
            }
            catch(Exception e)
            {
               e.printStackTrace();
            }
         }
   }

}
