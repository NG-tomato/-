//App�쐬��Main�N���X

//Java �� GUI �c�[���L�b�g�ł��� AWT ���g����������
import javax.swing.*;
import java.util.Scanner;

//Java �� GUI�A�v���P�[�V�������쐬���邽�߂̃N���X���C�u����(java.awt)����AContainer�N���X���i�[���邽�߂�Container��import
import java.awt.Container;

public class MainApp {

	public MainApp(int number){
		if(number == 1){
			MainPanel panel = new MainPanel();
		}else if(number == 2){
			MainApp2 panel = new MainApp2();
			panel.setVisible(true);
		}
	}

//Main�N���X
	public static void main(String[] args){
		//�����o��
		System.out.println("Please select number");
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