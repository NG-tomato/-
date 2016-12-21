//�\��������p�l�����쐬����N���X

//Java �� GUI �c�[���L�b�g�ł��� AWT ���g����������
import javax.swing.*;

//Java �� GUI�A�v���P�[�V�������쐬���邽�߂̃N���X���C�u����
import java.awt.*;

//AWT �R���|�[�l���g�ɂ���ăg���K�[�i�N���j����邳�܂��܂Ȏ�ނ̃C�x���g����������C���^�t�F�[�X�ƃN���X��񋟂���
import java.awt.event.*;

//���ʂ̏����̃��\�b�h(�����@�\�A���ۉ��A�����W�F�l���[�^)���W�߂��N���X�i���[�e�B���e�B�N���X�j
import java.util.*;

/*
�p�l���쐬���s���N���X���p��
implements �Ŏ󂯎��C�x���g����������
implements�̏ꍇ�A�C���^�[�t�F�C�X�Œ�`���ꂽ���\�b�h�����ׂĎ�������K�v������(�Ȃ��ƃG���[�ɂȂ�)���߁A���ʂ̋K�i�Ƃ��Ĉ�����
MouseListener �̓}�E�X�C�x���g���󂯎��N���X
Observer �͂���I�u�W�F�N�g�̕ω�������Ɉˑ�����I�u�W�F�N�g�ɒm�点��N���X
*/
public class MainPanel extends JPanel implements MouseListener, Observer{
	
	//�}�X1���Ƃ̑傫��
	static final int SIZE = 50;
	
	//�u����}�X��(�c������)�̐��i�����̂݁j
	//�T�C�Y�ύX�ł���悤�ɂ���
	static final int Squares = 8;
	
	//�}�X�ڑS�̂̉��̑傫��
	static final int W = SIZE * Squares;
	//�}�X�ڑS�̂̏c�̑傫��
	static final int H = SIZE * Squares;
	
	//��Ԃ�\���N���Xstate���쐬
	GameState state = new GameState(Squares);
	
	//�����_���őł�AI�̃N���XRandomCPU���쐬
	//RandomCPU cpu = new RandomCPU(Squares);
	
	//���C���p�l�����쐬���郁�\�b�h
	public MainPanel(){
		
		/*
		�p�l���̃T�C�Y��ݒ肷��
		Dimension �̓T�C�Y��\���N���X�ŁADimension(���̑傫��,�c�̑傫��)�ƕ\��
		*/
		setPreferredSize(new Dimension(W,H));
		
		/*
		�}�E�X�C�x���g�̏�����ǉ�����
		addMouseListener(MouseLisner l);
		l�̓}�E�X�C�x���g���󂯎��N���X�ŁAnull�̏ꍇ�͗�O�̓X���[���ꂸ�A���������s����Ȃ�(�܂�w�肵�Ȃ��̂Ɠ���)
		this�̏ꍇ�́A�������g�̃N���X�Ŏ󂯎��(addMouseListener�N���X���󂯎���ď�������)
		*/
		addMouseListener(this);
		
		/*
		�Ď������I�u�W�F�N�g���w�肷��N���X
		�Ď������I�u�W�F�N�g�̃f�[�^��\���Ă���
		update ���\�b�h���Ăяo�����ƂŁAObservable �� notifyObservers ���\�b�h(�Ď����Ă��郁�\�b�h)�ɕύX��ʒm����
		
		addObserver(Observer o)
		�I�u�W�F�N�g�̃I�u�U�[�o�Z�b�g�ɃI�u�U�[�o(�Ď��Ώ�)��ǉ�����
		��Ԃ�\���N���Xstate��Observable���p�����č���Ă��邽�߁A���ꂪ�Ď��ΏۂɂȂ�
		*/
		state.addObserver(this);
	}
	
	/*
	�`�ʂ��s�����\�b�h
	JComponent�N���X�Œ�`����Ă��郁�\�b�h�ŃR���|�[�l���g�̕`�悪�K�v�ɂȂ������ɓ����I�ɌĂяo�����
	JPanel���p�������N���X�Ȃ̂ŁA���̂܂܋L�q���邱�Ƃŕ`�ʂł���
	
	Graphics �I�u�W�F�N�g�́AJava ���T�|�[�g�����{�I�ȕ`�摀��ɕK�v�ȏ�ԏ����J�v�Z������������
	�`��Ώۂ̃R���|�[�l���g�A�F�A�t�H���g�Ȃǂ�ێ�����
	*/
	public void paintComponent(Graphics g){
		
		//�w�i
		//�F�w�� setColor(Color c)  ����ȍ~�ɕ`�ʂ�����̂̐F���w�肷��
		g.setColor(Color.LIGHT_GRAY);
		
		//�h��Ԃ� fillRect(�J�n�_�����W, �J�n�_�����W, �I���_x���W, �I���_y���W)
		g.fillRect(0, 0, W, H);
		
		//��
		//�F�w��
		g.setColor(Color.BLACK);
		
		/*
		���ڂ�������������
		�����`�ŏc���������Ȃ̂ŁA����for���̒��ŕ\��������
		����̂ق����珇�ԂɋL�q���Ă���
		*/
		for(int i=0; i<Squares; i++){
			//drawLine(x1, y1, x2, y2);
			//�_(x1, y1)�Ɠ_(x2, y2)������Ō���
			g.drawLine(0, i*SIZE, W, i*SIZE);
			g.drawLine(i*SIZE, 0, i*SIZE, H);
		}
		
		//�{�[�h���3���ڂ��炢�ɓ����Ă�F�̈Ⴄ���i�����Ă��Ȃ��Ă��Q�[�����͕ς��Ȃ��j
		//g.setColor(Color.DARK_GRAY);
		//g.drawRect(SIZE*2, SIZE*2, SIZE*4, SIZE*4);
		
		//��
		//�Q�[���̏�Ԃ�����state�N���X��data���\�b�h�����ォ�珇�ԂɒT�����Ă������ƂŁA��u���Ă���ꏊ�ƍ������������m����
		for(int y=0; y<Squares; y++){
			for(int x=0; x<Squares; x++){
				if(state.data[x][y] == 1){
					//���̋��\��
					g.setColor(Color.BLACK);
					/*
					�~��h��Ԃ�
					fillOval(x, y, w, h);
					�_(x, y)��������Ƃ��镝w�A����h�̒����`�ɓ��ڂ���ȉ~��`��
					*/
					g.fillOval(x*SIZE, y*SIZE, SIZE, SIZE);
				}else if(state.data[x][y] == -1){
					//���̋��\��
					g.setColor(Color.WHITE);
					g.fillOval(x*SIZE, y*SIZE, SIZE, SIZE);
				}
			}
		}
		
		//�f�[�^�\��
		g.setColor(Color.RED);
		/*
		�_ (x, y) ���������Ƃ��钷���`�̈�ɕ������`��
		drawString("������", x, y);
		�_(x, y)���������Ƃ��钷���`�̈�ɕ������`��
		
		�����T�C�Y��t�H���g���w�肵�����ꍇ�͈ȉ��̂悤�ɋL�q
		Font font = new Font("Arrival", Font.BOLD, 30);
		Font(�t�H���g���A�t�H���g�X�^�C���A�t�H���g�̃T�C�Y)
		g.setFont(font);
		*/
		
		g.drawString("TURN = "+state.turn, 10, 30);
		g.drawString("PLAYER = "+state.player, 10, 50);
		g.drawString("DISC = "+state.black+" : " +state.white, 10, 70);
		
	}
	
	/*
	�Ď�����Observar�̍X�V��ʒm����N���X
	update(Observable o, Object arg);
	Update(�Ď��\�ȃI�u�W�F�N�g, �ʒm�����N���X�ɓn������)
	*/
	public void update(Observable o, Object arg){
		/*
		�ĕ`�ʃC�x���g�������������ɌĂяo����郁�\�b�h�Bupdate���\�b�h���Ăяo���B
		�Ăт����ꂽupdate���\�b�h�͉�ʂ��N���A�������paint���\�b�h���Ăяo���A�ĕ`�ʂ��s���B
		repaint();
		
		�f�[�^�̓��e���ω����Ă���̂ŁA�ĕ`�ʂ��邾���ŕ`�ʓ��e�����f�����
		*/
		repaint();
	}
	
	//�R���|�[�l���g��Ń}�E�X�{�^�����������ƌĂяo�����N���X
	public void mousePressed(MouseEvent e){
		
		//�C�x���g�����������Ƃ���x���W���擾
		int x = e.getX();
		//�C�x���g�����������Ƃ���y���W���擾
		int y = e.getY();
		
		//�ǂ��̈ʒu�ɒu�����������m�������ʒm
		x /= SIZE;
		y /= SIZE;
		//�W���o�́i�R���\�[���i���Ԃ�R�}���h�v�����v�g�j�ɏo�́j
		System.out.println("(x,y)=("+x+","+y+")");
		
		//���u�����u���Ȃ��ꍇ�̓��b�Z�[�W
		//state.put(x,y) �͂���(x,y)�̍��W�n�_�ɒu�����Ƃ��ł��邩�`�F�b�N���郁�\�b�h(GameState���̃��\�b�h)
		if( state.put(x,y) == false ){
			/*
			���b�Z�[�W�_�C�A���O��\��
			JOptionPane.showMessageDialog(c, s);
			c = Container�I�u�W�F�N�g
			JFrame���p�����Ă���N���X�Ȃ̂ŁA���̒���Container�I�u�W�F�N�g���p�����Ă��� ���̂��߁Athis �ł��̃N���X����Container��I���ł���
			s = "�\�����镶����"
			*/
			JOptionPane.showMessageDialog(this, "Can't put this area!");
		}
		
		//�Ֆʂ����܂�A�܂��͂ǂ��炩�̋0�̂Ƃ��I��
		if(state.turn == (Squares * Squares) - 4){
			JOptionPane.showMessageDialog(this, "End!");
		}
		/*
		�p�X�`�F�b�N
		state�N���X����checkPass���\�b�h�Œu�����Ƃ��\�ȏꏊ�����邩�ǂ����T��
		*/
		else if( state.checkPass() == true ){
			/*
			state��player�ϐ��́A1��-1���Ŏ�Ԃ𔻒f���Ă���̂�-1���|���邱�ƂŎ�Ԃ�ς��邱�Ƃ��ł���
			*/
			state.player *= -1;
			/*
			player�̂ǂ�����p�X�̂Ƃ��͂���ȏ�u���Ȃ��̂ŁA�I��������
			*/
			if(state.checkPass() == true){
				JOptionPane.showMessageDialog(this, "End!");
			}
			JOptionPane.showMessageDialog(this, "Pass! Next turn is "+state.player);
		}
		/*
		//CPU�̃^�[��
		if(state.player == cpu.color){
			//cpu����decide���\�b�h�Œu���ꏊ������
			int action[] = cpu.decide(state);
			
			//���W���ȊO�Œu����ꏊ������ꍇ�̂݋��u������������
			if(action[0] != -1)
				state.put(action[0], action[1]);
			
			//�Ֆʂ����܂�����I��
			if(state.turn == (Squares * Squares) - 4){
				JOptionPane.showMessageDialog(this, "End!");
			}
			//�p�X�`�F�b�N
			else if( state.checkPass() == true ){
				state.player *= -1;
				//�����p�X���ƏI��
				if(state.checkPass() == true){
					JOptionPane.showMessageDialog(this, "End!");
				}
				JOptionPane.showMessageDialog(this, "Pass! Next turn is "+state.turn);
			}
		}
		*/
	}
	
	public void mouseClicked(MouseEvent e){

	}
	public void mouseReleased(MouseEvent e){

	}
	public void mouseEntered(MouseEvent e){

	}
	public void mouseExited(MouseEvent e){

	}
}