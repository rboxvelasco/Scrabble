package persistencia;

import domini.jugadors.*;
import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Classe per deserialitzar instàncies de {@link Jugador} a partir de JSON utilitzant GSON.
 *
 * <p>Aquesta classe implementa {@link JsonDeserializer} i permet convertir automàticament
 * un element JSON a una subclasse específica de {@code Jugador}, com {@code Avatar} o {@code Maquina},
 * segons el valor del camp {@code "tipus"} dins del JSON.</p>
 */
public class JugadorDeserializer implements JsonDeserializer<Jugador> {
    
    /**
     * Deserialitza un objecte JSON en una instància de {@link Jugador}.
     *
     * <p>El JSON ha de contenir un camp {@code "tipus"} que indica si el jugador és un
     * {@code "avatar"} o una {@code "maquina"}. Segons aquest valor, es deserialitza
     * com un {@link Avatar} o {@link Maquina} respectivament.</p>
     *
     * @param json     L'element JSON a deserialitzar.
     * @param typeOfT  El tipus esperat de l'objecte (no utilitzat directament).
     * @param context  El context de deserialització de GSON.
     * @return Una instància de {@link Avatar} o {@link Maquina}.
     * @throws JsonParseException Si el tipus de jugador és desconegut.
     */
    @Override
    public Jugador deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        String tipus = obj.get("tipus").getAsString();

        switch (tipus) {
            case "avatar":
                return context.deserialize(obj, Avatar.class);
            case "maquina":
                return context.deserialize(obj, Maquina.class);
            default:
                throw new JsonParseException("Tipus de jugador desconegut: " + tipus);
        }
    }
}