package REST.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlAccessorType (XmlAccessType.FIELD)
public class Dictionary {

    @XmlElement (name = "Dictionary")
    private List<Word> dizionario;

    private static Dictionary instance;

    private Dictionary() {
        dizionario = new ArrayList<Word>();
    }

    //singleton per ritornare l'istanza di dizionario
    public synchronized static Dictionary getInstance() {
        if (instance==null)
            instance = new Dictionary();
        return instance;
    }
    public synchronized void add(Word w) {
        dizionario.add(w);
    }

    public synchronized List<Word> getDizionario() {
        return new ArrayList<>(dizionario);
    }

    public Word getDefinition(String word) {
        List<Word> copy = getDizionario();
        for (Word w: copy) {
            if (w.getWord().equals(word)) {
                return w;
            }
        }
        return null;
    }

    public synchronized void deleteWord(String word) {
        for (int i = 0; i < dizionario.size(); i++) {
            if (dizionario.get(i).getWord().equals(word))
                dizionario.remove(i);
        }
    }

    public synchronized int checkWord(Word parola) {
        List<Word> copy = getDizionario();
        for (int i = 0; i < copy.size(); i++) {
            Word w = copy.get(i);
            if (w.getWord().equals(parola.getWord()))
                return i;
        }
        return -1;
    }

    public synchronized void updateDefinition(Word w) {
        int posParola = this.checkWord(w);
        dizionario.set(posParola, w);

    }
}
