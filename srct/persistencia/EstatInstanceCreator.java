package persistencia;

import com.google.gson.InstanceCreator;
import java.lang.reflect.Type;
import domini.scrabble.Estat;
import domini.scrabble.EstatEnCurs;

/**
 * 
 * Implementació de la interfície InstanceCreator per proporcionar una instància d'EstatEnCurs
 * quan es deserialitza un objecte de tipus Estat.
 *
 * Aquesta classe és útil quan es treballa amb Gson per deserialitzar jerarquies de classes.
 * En aquest cas, quan Gson trobi un camp de tipus {@link Estat}, s'utilitzarà {@link EstatEnCurs}
 * com a implementació concreta.
 *
 * @see com.google.gson.InstanceCreator
 * @see domini.scrabble.Estat
 * @see domini.scrabble.EstatEnCurs
 */
public class EstatInstanceCreator implements InstanceCreator<Estat> {
    
    /**
     * Crea una nova instància de la subclasse {@link EstatEnCurs} de {@link Estat}.
     *
     * @param type El tipus genèric que s'ha de crear (ignorat en aquest cas).
     * @return Una nova instància de {@link EstatEnCurs}.
     */
    @Override
    public Estat createInstance(Type type) {
        return new EstatEnCurs();
    }
}
