//App�쐬��Main�N���X

//Java �� GUI �c�[���L�b�g�ł��� AWT ���g����������
import javax.swing.*;
import java.util.Scanner;

//Java �� GUI�A�v���P�[�V�������쐬���邽�߂̃N���X���C�u����(java.awt)����AContainer�N���X���i�[���邽�߂�Container��import
import java.awt.Container;

public class MainApp {

	public MainApp(int number){
		if(number == 1){
			//�����o��
			Scanner scan = new Scanner(System.in);
			/*
			String player = "Black";
			int B_CPU = 0;
			int W_CPU = 0;
			for(int i = 0; i < 2; i++){
				System.out.println("Please select use " + player + " CPU");
				System.out.println("RondomCPU     : 1");
				System.out.println("MonteCarloCPU : 2");
				int count = scan.nextInt();
				player = "White";
				if(i == 0){
					B_CPU = count;
				}else{
					W_CPU = count;
				}
			}
			*/
			//�����o��
			System.out.println("Please input roop count");
			//�J��Ԃ��񐔓ǂݍ���
			int count = scan.nextInt();
			MainPanel panel = new MainPanel(count);
		}else if(number == 2){
			
			MainApp2 panel = new MainApp2();
			panel.setVisible(true);
			
		}
	}

//Main�N���X
	public static void main(String[] args){
		//�����o��
		System.out.println("Please select use GUI");
		System.out.println("Not need GUI : 1");
		System.out.println("Need GUI : 2");
		//�l�擾
		Scanner scan = new Scanner(System.in);
		int val = scan.nextInt();
		//MainApp�N���X���Ăяo���A��ʂ��쐬
		if(val == 1 || val == 2){
			MainApp app = new MainApp(val);
		}else{
			System.out.println("Unspecified character was entered");
		}
		
	}
	
}