//Thiago Santos Teixeira nUSP 10736987

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private static final int R =16;
    private static final int G = 8;
    private static final int B = 0;
    private static final double BORDER_ENERGY = 1000.0;
    private final int[] pic;
    private int largura, altura;
    private double[] energy;
    private boolean isTransposed = false; //esta transposta?
    private boolean isHorizontal = false; //eh horizontal?

    //----------------------------------------------------//
    
    //FUNCOES UTILITARIAS(utilizei i, j ao inves de x, y por costume):

    //devolve a cor do ponto
    private int getRGBColor(int i, int j, int channel) {
        return pic[ijUmD(i, j)] >> channel & 0xFF;
    }

    //calcula o gradiente duplo da energia em (i, j)
    private double calcEnergy(int i, int j) {
        if (i == 0 || j == 0 || i == largura - 1 || j == altura - 1) return BORDER_ENERGY;
        return Math.sqrt(
                Math.pow(getRGBColor(i + 1, j, R) - getRGBColor(i - 1, j, R), 2)
                        + Math.pow(getRGBColor(i + 1, j, G) - getRGBColor(i - 1, j, G), 2)
                        + Math.pow(getRGBColor(i + 1, j, B) - getRGBColor(i - 1, j, B), 2)
                        + Math.pow(getRGBColor(i, j + 1, R) - getRGBColor(i, j - 1, R), 2)
                        + Math.pow(getRGBColor(i, j + 1, G) - getRGBColor(i, j - 1, G), 2)
                        + Math.pow(getRGBColor(i, j + 1, B) - getRGBColor(i, j - 1, B), 2)
        );
    }

    //valida a picture
    private static void validate(Picture picture) {
        if (picture == null) throw new IllegalArgumentException("Picture argument can not be null");
    }

    //valida os indices
    private static void validate(int idx, int bound) {
        if (idx < 0 || idx >= bound)
            throw new IllegalArgumentException("Index " + idx + " is not between 0 and " + (bound - 1));
    }

    private void validate(int[] seam) {
        if (largura <= 1) throw new IllegalArgumentException("the width or height of the picture is <= 1");
        if (seam == null) throw new IllegalArgumentException("argument can not be null");
        if (seam.length != altura) throw new IllegalArgumentException("argument array is of length" + largura);

        for (int i = 0; i < seam.length; i++) {
            validate(seam[i], largura);
            if (i > 0 && Math.abs(seam[i] - seam[i - 1]) > 1) throw new IllegalArgumentException("Two adjacent entries differ by more than 1");
        }
    }

    private int ijUmD(int i, int j) {
        validate(i, largura);
        validate(j, altura);
        return !isTransposed ? i + largura * j : j + i * altura;
    }

    private void checaTransposta() {
        if (isHorizontal ^ isTransposed) {   //excludentes, portanto o XOR
            isTransposed = !isTransposed;
            int temp = altura;
            altura = largura;
            largura = temp;
        }
    }
    //retorna o indice de x e y dps do removeSeam
    private int xyTo1DAfterwards(int x, int y) {
        validate(x, largura);
        validate(y, altura);
        return !isTransposed ? x + (largura - 1) * y : y + x * (altura - 1);
    }

    //---------------------------------------------------------//
    //FUNCOES DA API:


    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture){
        validate(picture);
        largura = picture.width();
        altura = picture.height();
        pic = new int[largura * altura];
        energy = new double[altura*largura];
        for(int i = 0; i< largura; i++){
            for(int j = 0; j < altura; j++) pic[ijUmD(i, j)] = picture.getRGB(i, j);
        }
        for (int i = 0; i<largura; i++){
            for(int j=0; j<altura; j++) energy[ijUmD(i, j)] = calcEnergy(i, j);     //precisa de um segundo for ja que calcEnergy precisa de uma fullpic
        }
    }
 
    // current picture
    public Picture picture(){
        checaTransposta();
        Picture picture = new Picture(largura, altura);
        for(int i = 0; i<largura; i++){
            for(int j=0; j<altura; j++) picture.setRGB(i, j, pic[ijUmD(i, j)]);
        }
        return picture;
    }
 
    // width of current picture
    public int width(){
        checaTransposta();
        return largura;
    }
 
    // height of current picture
    public int height(){
        checaTransposta();
        return altura;
    }
 
    // energy of pixel at column x and row y
    public double energy(int x, int y){
        checaTransposta();
        validate(x, largura);
        validate(y, altura);
        return energy[ijUmD(x, y)];
    }
 
    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam(){
        isHorizontal = true;
        checaTransposta();
        int[] seam = findVerticalSeam();
        isHorizontal = false;
        return seam;
    }
 
    // sequence of indices for vertical seam
    public int[] findVerticalSeam(){
        checaTransposta();
        int[] seam = new int[altura];
        if(altura<=2) return seam;
        int[] edgeTo = new int[largura * altura];
        double[] distTo = new double[largura * altura];
        for(int i = 0; i< largura; i++){
            for(int j=0; j< altura; j++) distTo[ijUmD(i,j)] = Double.POSITIVE_INFINITY; //infinito
        }

        for (int j = 1; j< altura; j++){
            for(int i = 0; i<largura;i++){
                if(i >0 && distTo[ijUmD(i-1,j)] > energy[ijUmD(i-1, j)] +distTo[ijUmD(i, j-1)]){
                    distTo[ijUmD(i-1, j)] = energy[ijUmD(i-1, j)] + distTo[ijUmD(i, j -1)];
                    edgeTo[ijUmD(i-1, j)] = i;
                }
                if (distTo[ijUmD(i, j)] > energy[ijUmD(i, j)] + distTo[ijUmD(i, j - 1)]) {
                    distTo[ijUmD(i, j)] = energy[ijUmD(i, j)] + distTo[ijUmD(i, j - 1)];
                    edgeTo[ijUmD(i, j)] = i;
                }
                if (i < largura - 1 && distTo[ijUmD(i + 1, j)] > energy[ijUmD(i + 1, j)] + distTo[ijUmD(i, j - 1)]) {
                    distTo[ijUmD(i + 1, j)] = energy[ijUmD(i + 1, j)] + distTo[ijUmD(i, j - 1)];
                    edgeTo[ijUmD(i+1, j)] = i;
                }
            }
        }
        //encontra com menos energia
        for(int i =1; i<largura -1; i++){
            if(distTo[ijUmD(seam[altura-2], altura-2)] > distTo[ijUmD(i, altura -2)]){
                seam[altura -2 ]= i;
            }
        }

        //backtrack usando edgeTo
        for(int j = altura-2; j>0; j--) seam[j-1] = edgeTo[ijUmD(seam[j], j)];

        seam[altura -1] = Math.max(seam[altura-2]-1, 0);
        return seam;
    }


    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam){
        isHorizontal =true;
        checaTransposta();
        validate(seam);
        removeVerticalSeam(seam);
        isHorizontal = false;
    }
 
    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam){
        checaTransposta();
        validate(seam);
        if(!isTransposed){

            for (int y = 0; y < altura; y++) {
                System.arraycopy(pic, ijUmD(seam[y], y) + 1, pic, xyTo1DAfterwards(seam[y], y),
                        largura - seam[y] - 1 + (y < altura - 1 ? seam[y + 1] : 0));


                System.arraycopy(energy, ijUmD(seam[y], y) + 1, energy, xyTo1DAfterwards(seam[y], y),
                        largura - seam[y] - 1 + (y < altura - 1 ? seam[y + 1] : 0));
                        
                if (seam[y] == 0) energy[xyTo1DAfterwards(0, y)] = BORDER_ENERGY;
            }
        } else {

            for (int y = 0; y < altura; y++) {
                for (int x = 0; x < largura; x++) {
                    if (x > seam[y]) {
                        pic[ijUmD(x - 1, y)] = pic[ijUmD(x, y)];
                        energy[ijUmD(x - 1, y)] = energy[ijUmD(x, y)];
                    }
                }
                if (seam[y] == 0) energy[ijUmD(0, y)] = BORDER_ENERGY;
            }
        }
        largura--;

        //recalcula energies dos pontos removidos
        for (int y = 1; y < altura - 1; y++) {
            if (seam[y] > 1) energy[ijUmD(seam[y] - 1, y)] = calcEnergy(seam[y] - 1, y);
            if (seam[y] < largura - 1) energy[ijUmD(seam[y], y)] = calcEnergy(seam[y], y);
        }
    }
}