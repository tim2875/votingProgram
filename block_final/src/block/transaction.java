package block;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.ECGenParameterSpec;
import java.util.Base64;

public class transaction {
	//A가 V에 투표하였다 -> sender=A, receiver=V의 index(블록체인 고유번호)
	
	public String transactionID;	// 트랜잭션 해쉬 암호화
	public String sender;	//송신자
	public String receiver;		//접근한 투표의 인덱스
	public byte[] signature;	//송신자 서명
	public PublicKey sender_pubkey;	//송신자의 공개키
	
	private static int sequence=0;	//해시값 중복을 피하기 위함 + 트랜잭션 갯수 카운트 저장용
	
	transaction(String imei, String data)
	{
		try{
			this.sender=imei;
			this.receiver=data;
			signature=encrypt(sender);
			transactionID=cal_Hash();
			System.out.println("ID = "+transactionID);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	private String cal_Hash()
	{
		sequence++;
		System.out.println("sequence"+sequence);
		String string_signature = new String(this.signature);
		//송신자의 공개키, 투표 인덱스를 base64로 인코딩 + 서명 + sequence값으로 해시 생성
		String hash = block.Sha256(
		Base64.getEncoder().encodeToString(sender_pubkey.getEncoded()) + Base64.getEncoder().encodeToString(receiver.getBytes()) 
		+ string_signature + Integer.toString(sequence));
		return hash;
	}
	
	//System.out.println("암호화 문장 : 0x" + (new BigInteger(1, signatureByte).toString(16)).toUpperCase());
	private byte[] encrypt(String data2) throws Exception{			//서명(암호화)하는 함수
		KeyPairGenerator kpg;
		kpg=KeyPairGenerator.getInstance("EC", "SunEC");
		
		ECGenParameterSpec ecsp; 
		ecsp = new ECGenParameterSpec("sect163k1");
		kpg.initialize(ecsp, new SecureRandom());	//키 생성 
		KeyPair kp = kpg.genKeyPair();  
		PrivateKey privKey = kp.getPrivate(); 	//개인키 생성  	
		PublicKey pubKey = kp.getPublic();	//공개키 생성  
		
		this.sender_pubkey = pubKey;		//공개키 저장
		
		Signature ecdsa;
		ecdsa = Signature.getInstance("SHA1withECDSA", "SunEC");		//개인키 설정  
		ecdsa.initSign(privKey);
		ecdsa.update(data2.getBytes("UTF-8"));
		byte[] signatureByte = ecdsa.sign();	//원래 문장 암호화 
		
		
		return signatureByte;
	}
	
	
	//System.out.println("원래 문장 검증 : "+ verifySig(pubKey, data2, signatureByte));
	public static boolean verifySig(PublicKey publicKey, String data, byte[] signatureByte) {		//검증화 함수
		try {
			Signature signature;		
			signature = Signature.getInstance("SHA1withECDSA", "SunEC");
			signature.initVerify(publicKey);				//publicKey로 검증
			signature.update(data.getBytes("UTF-8"));		//data를 가져옴
			return signature.verify(signatureByte);			//검증한 것이 true or false인지 반환
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
}
