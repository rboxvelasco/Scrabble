package persistencia;

import com.google.gson.*;
import domini.jugadors.*;
import java.lang.reflect.Type;

/**
 * Classe per serialitzar instàncies de {@link Jugador} a JSON utilitzant GSON.
 *
 * <p>Aquesta classe implementa {@link JsonSerializer} i s'encarrega d'afegir un camp
 * identificador del tipus ({@code "tipus"}) per tal que es pugui deserialitzar correctament
 * més endavant. Segons si el jugador és un {@link Avatar} o una {@link Maquina},
 * s'afegeix la informació corresponent.</p>
 */
public class JugadorSerializer implements JsonSerializer<Jugador> {
    
    /**
     * Serialitza una instància de {@link Jugador} a JSON, afegint un camp {@code "tipus"}
     * per identificar la seva subclasse concreta.
     *
     * <p>El resultat és un objecte JSON amb dues claus:</p>
     * <ul>
     *   <li>{@code "tipus"} – conté una cadena que indica si és un {@code "avatar"} o {@code "maquina"}.</li>
     *   <li>{@code "data"} – conté la serialització específica de la subclasse del jugador.</li>
     * </ul>
     *
     * @param src         L'objecte {@link Jugador} a serialitzar.
     * @param typeOfSrc   El tipus de l'objecte (no s'utilitza directament).
     * @param context     El context de serialització proporcionat per GSON.
     * @return Un {@link JsonElement} que representa l'objecte serialitzat.
     * @throws JsonParseException Si el tipus concret de jugador no és reconegut.
     */
    @Override
    public JsonElement serialize(Jugador src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        
        // Add the type identifier
        if (src instanceof Avatar) {
            jsonObject.addProperty("tipus", "avatar");
        } else if (src instanceof Maquina) {
            jsonObject.addProperty("tipus", "maquina");
        } else {
            throw new JsonParseException("Unknown Jugador type: " + src.getClass().getName());
        }
        
        // Serialize the object with its specific attributes
        JsonElement element = context.serialize(src, src.getClass());
        jsonObject.add("data", element);
        
        return jsonObject;
    }
}