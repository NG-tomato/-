//App�쐬��Main�N���X

//Java �� GUI �c�[���L�b�g�ł��� AWT ���g����������
import javax.swing.*;

//Java �� GUI�A�v���P�[�V�������쐬���邽�߂̃N���X���C�u����(java.awt)����AContainer�N���X���i�[���邽�߂�Container��import
import java.awt.Container;

//JFrame���p�����邱�Ƃł��낢��ȗ�
public class MainApp2 extends JFrame {

	public MainApp2(){
		//�t���[���̃^�C�g���i��ɕ\��������j�����߂�
		setTitle("Othello");
		//�~��ŏ������I��
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		/*
		�p�l�����쐬
		�p�l���̓��e�����߂�MainPanel�N���X���Ăяo���č쐬����
		
		#�p�l�� �{�^���⃉�x���Ȃǂ̑��̃R���|�[�l���g��\��t������A���C�A�E�g��ݒ肷�邱�Ƃ��ł���
		��
		�������̃R���|�[�l���g���܂Ƃ߂ă��C�A�E�g��ݒ肵�����ꍇ�Ȃǂɗ��p����
		
		#�R���|�[�l���g ���̃v���O��������Ăяo���ꂽ��A�����ꂽ�肵�Ďg�p�����v���O�������i
		*/
		MainPanel2 panel = new MainPanel2();
		
		/*
		JFrame�N���X�̃I�u�W�F�N�g����ContentPane�����o��
		
		#ContentPane �{�^���Ȃǂ̕\�����s���R���|�[�l���g��ǉ����ĕ\��������ꏊ
		*/
		Container contentPane = getContentPane();
		
		/*
		�R���|�[�l���g�̒ǉ�
		��ō쐬�����p�l����ǉ�
		*/
		contentPane.add(panel);
		
		//�����̗̈悩��T�C�Y�����߂�
		pack();
	}
}