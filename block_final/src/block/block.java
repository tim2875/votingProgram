package block;
import java.security.MessageDigest;

public class block {
	String hash;					// 해시값  
	String data;	 				// 블럭의 data		-> 투표값
	blockHeader blockHeader;		//블록 헤더 변수
	
	public block(String data, String pre_hash)		//block 생성자
	{
		this.blockHeader = new blockHeader(pre_hash); 			//블록헤더 생성
		this.data = data;										//투표값 예정
		this.hash = cal_Hash();
	}
	
	public String cal_Hash()									//해쉬계산함수 
	{
		//기존해쉬에 시간값과 암호화 임시값과 블록데이터를 더해 재암호화 => 다음 블록의 해쉬함수 생성.
		String cal_hash = Sha256(blockHeader.pre_Hash + Long.toString(blockHeader.timestamp) + Integer.toString(blockHeader.nonce) + this.data);	
		return cal_hash;
	}
	
	public static String Sha256(String input)	//sha256방식의 암호화 함수 
	{
		try 
		{
			MessageDigest digest = MessageDigest.getInstance("SHA-256");	//sha-256방식으로 암호화
			byte[] hash = digest.digest(input.getBytes());			//input값의 암호화된 값을 hash에 저장.
			StringBuffer hexString = new StringBuffer();					
			for (int i = 0; i < hash.length; i++) 
			{
				//암호화된 input값(hash)을 16진수 String으로 변경, 0xff비트연산을 통해 비트 앞자리가 1이 나올경우 음수로 인식하는 것 방지
				String hex = Integer.toHexString(0xff & hash[i]); 			
				if (hex.length() == 1)
				{
					hexString.append('0');									// hex값이 16보다 작을경우, 0삽입해 2자리 유지
				}
				hexString.append(hex);										// 그 외에 hex값 연결
			}
			return hexString.toString();
		} 
		catch (Exception e) 
		{
			System.out.print("exception : "+e);
			return "exception";
		}
	}	
	public void proof_of_work()		//작업증명
	{	
		//검증방법 = diff의 갯수만큼 해쉬의 앞자리가 0으로 채워지는것으로 검증.
		String proof = new String(new char[this.blockHeader.diff]).replace('\0', '0');			//diff만큼의 배열 생성
		while(true)
		{
			if(!(this.hash.substring(0, this.blockHeader.diff).equals(proof)))				//계산된 해시 검증과정
			{
				//재계산 후 검증 필요
				this.blockHeader.nonce++;
				this.hash=this.cal_Hash();
			}
			else
				break;
		}
		
	}
}
