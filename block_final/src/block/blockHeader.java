package block;

import java.util.Date;

public class blockHeader{
	
		//public String version;		// 소프트웨어 버전
		public String pre_Hash;		// 이전 블럭의 해시값
		public long timestamp; 		// 블록생성시간
		public int diff;			// 작업증명 난이도
		public int nonce;			// 채굴및 작업증명 카운터
		
	public blockHeader(String pre_Hash)
	{
		this.pre_Hash = pre_Hash;
		this.timestamp = new Date().getTime();
		this.diff=3;
		this.nonce=0;
	}
}
