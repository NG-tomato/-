public class Test{
	public static void main(String[] args){
	
		int size = 4;
		//�����l�i�^�񒆂̂S�����݂ɂ����ԁj���쐬
		int[][] data;
		data = new int[size + 2][size + 2];
		data[size/2][size/2] = 1;
		data[size/2][size/2 + 1] = -1;
		data[size/2 + 1][size/2] = -1;
		data[size/2 + 1][size/2 + 1] = 1;
		//�ǂ��쐬
		for(int i = 0;i < size + 1; i++){
			data[0][i] = 2;
			data[size + 1][i + 1] = 2;
			data[i + 1][0] = 2;
			data[i][size + 1] = 2;
		}
		System.out.println(data.length);
		for(int i = 0; i< size +2; i++){
			for(int j = 0; j < size + 2; j++){
				if(data[i][j] == 0){
					System.out.print(5 + " ");
				}else{
					System.out.print(data[i][j] + " ");
				}
			}
			System.out.println();
		}
	}
}
