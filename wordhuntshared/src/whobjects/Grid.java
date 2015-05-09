package whobjects;
/**
 * Created by Karim Ghozlani on 06.05.2015.
 */
public class Grid {

    private char[][] content;

    public Grid() {

    }

    public Grid(int size) {
        content = new char[size][size];
    }

    public void setContent(char[][] content) {
        this.content = new char[content.length][content.length];
        for (int i = 0; i < content.length ; i++) {
            for (int j = 0; j < content.length ; j++) {
                this.content[i][j] = content[i][j];
            }
        }
    }

    /**
	 * @return the content
	 */
	public char[][] getContent() {
		return content;
	}

    public String printGrid() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < content.length; i++) {
            sb.append('\n');
            for (int j = 0; j < content.length; j++) {
                sb.append(content[i][j] + " | ");
            }
        }
        sb.append('\n');
        return sb.toString();
    }
}
