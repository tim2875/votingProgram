package block;

import java.util.Date;
import java.util.HashMap;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.ECGenParameterSpec;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class blockchain {
	//나이제한
	int start_age;
	int limit_age;		
	String title; 		// 투표제목
	ArrayList<String> contents = new ArrayList<String>();		//투표 후보
	ArrayList<block> blockchain =  new ArrayList<block>();
	//시간제한
	String start_time = "";
	String limit_time = "";	
	ArrayList<transaction> transaction = new ArrayList<transaction>();
	
	
	public blockchain(int start_age,int limit_age, String title, int count, String start_time, String limit_time)
	{
		this.start_age = start_age;
		this.limit_age = limit_age;
		this.title = title;
		this.start_time = start_time;
		this.limit_time = limit_time;
		blockchain.add(new block("first block", "0"));				//첫번째 블록 생성(유권자 나이제한, pre_hash=0)
	}
	public void connect(String data, String imei, String index) 	//투표값,송신자,블록체인인덱스
	{
		transaction.add(new transaction(imei, index));
		blockchain.add(new block(data, blockchain.get(blockchain.size()-1).hash));		//블록 추가
		blockchain.get(blockchain.size()-1).proof_of_work();							//해시 검증
	}
	public Boolean isOverlab(String imei)
	{
		for(int i=0;i<transaction.size();i++)
		{
			if(transaction.get(i).sender.equals(imei))
			{
				//중복투표
				return false;
			}
		}
		return true;
	}
	
	public Boolean isChainValid(ArrayList<block> blockchain) 
	{
		block currentBlock; 
		block pre_Block;
		System.out.println("검증시작");
		//전체 블럭을 체크합니다.
		for(int i=1; i < blockchain.size(); i++) 
		{
			currentBlock = blockchain.get(i);
			pre_Block = blockchain.get(i-1);
			System.out.println(i+"번 블록검증");
			//현재 블럭의 hash가 맞는지 체크합니다.
			if(!currentBlock.cal_Hash().equals(currentBlock.hash))
			{
				System.out.println(i+"번째 블럭 해쉬 계산 오류");	
				//System.out.println(currentBlock.hash);
				//System.out.println(currentBlock.cal_Hash());
				return false;
			}
			//이전 블럭의 hash값과 동일한지 체크합니다.
			if(!pre_Block.hash.equals(currentBlock.blockHeader.pre_Hash))
			{
				System.out.println(i-1+"번째 이전 블럭 해쉬 비교오류");
				//System.out.println(currentBlock.blockHeader.pre_Hash);
				return false;
			}
		}
		return true;
	}	
	public int isTimeRunnable() throws ParseException
	{
		//1 = 가능  0 = 안열림  2 = 종료 
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
		now = dateFormat.parse(dateFormat.format(now));
		Date limit = dateFormat.parse(limit_time);
		Date start = dateFormat.parse(start_time);
		if((now.getTime()-start.getTime())<0)
		{
			return 0;
		}
		else if((limit.getTime()-now.getTime())<0)
		{
			return 2;
		}
		else 
			return 1;
	}
	
	/*
	public String vote_count()
	{
		int[] vote_all = new int[blockchain.size()+1];		//득표수 집계용(각각 1~n 번의 득표수)
		//초기화
		for(int i=0;i<vote_all.length;i++)
			vote_all[i]=0;
		int win_index = 0, max=0;			//1등 위치/득표율 계산용
		int same_ratio=0;		//동률일경우 대비
		ArrayList<Integer>same_index=new ArrayList<Integer>();
		
		//집계 시작
		for(int i=1;i<blockchain.size();i++)		//0번 블럭 = 초기블럭
		{
			int vote = Integer.parseInt(blockchain.get(i).data);
			vote_all[vote]++;
			if(max<vote_all[vote])
			{
				max=vote_all[vote];
				win_index = vote;
				same_ratio=0;
				same_index.clear();
			}
			else if(max==vote_all[vote])
			{
				same_ratio=1;
				same_index.add(vote);
			}
		}
		String ans="";
		if(same_ratio==1)
		{
			ans = String.format("동률 : %d", win_index);
			for(int i=0;i<same_index.size();i++)
				ans+=String.format(", %d", same_index.get(i));
		}
		else
		{
			if(blockchain.size()==1)
				ans = "투표기록이 없음"; 
			else
				ans = String.format("%d", win_index);	//(n번)
		}
		
		return ans;
	}
	*/
	
	public String vote_sum()
	{
		int[] vote_all = new int[contents.size()+1];		//득표수 집계용(각각 1~n 번의 득표수)
		//초기화
		for(int i=0;i<vote_all.length;i++)
			vote_all[i]=0;
		
		//집계 시작
		for(int i=1;i<blockchain.size();i++)		//0번 블럭 = 초기블럭
		{
			int vote = Integer.parseInt(blockchain.get(i).data);
			vote_all[vote]++;
		}
		String ans="";
		for(int i=0;i<vote_all.length;i++)
			ans += Integer.toString(vote_all[i])+"/";
		return ans;
	}
}
