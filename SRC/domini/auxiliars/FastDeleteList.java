package domini.auxiliars;

import java.util.*;

/**
 * FastDeleteList és una estructura de dades que permet insercions,
 * eliminacions i accessos indexats en temps constant. Permet emmagatzemar
 * elements duplicats, però no manté l'ordre d'inserció.
 * 
 * @param <T>   Tipus d'element que emmagatzema la llista
 */
public class FastDeleteList<T> {
    private final List<T> list;
    private transient Map<T, Set<Integer>> indexMap;

    /**
     * Crea una FastDeleteList buida.
     */
    public FastDeleteList() {
        list = new ArrayList<>();
        indexMap = new HashMap<>();
    }

    /**
     * Afegeix un element al final de la llista.
     * 
     * @param value Element a afegir.
     */
    public void add(T value) {
        list.add(value);
        indexMap.computeIfAbsent(value, k -> new HashSet<>()).add(list.size() - 1);
    }

    /**
     * Elimina una ocurrència de l'element donat, si existeix.
     * 
     * @param value Element a eliminar.
     * @return true si s'elimina, false si no hi era.
     */
    public boolean remove(T value) {
        Set<Integer> indices = indexMap.get(value);
        if (indices == null || indices.isEmpty()) return false;

        int indexToRemove = indices.iterator().next();
        return removeAt(indexToRemove);
    }

    /**
     * Afegeix tots els elements de la llista proporcionada.
     * 
     * @param values Lista d'elements a afegir.
     */
    public void addAll(List<T> values) {
        for (T value : values) {
            add(value);
        }
    }

    /**
     * Elimina l'element en la posició indicada.
     * 
     * @param index Índex de l'element a eliminar.
     * @return      True si s'elimina correctament, False en cas contrari.
     * @throws IndexOutOfBoundsException Si l'índex es invàlid.
     */
    public boolean removeAt(int index) {
        if (index < 0 || index >= list.size()) {
            throw new IndexOutOfBoundsException("Índex fora de rang: " + index);
        }

        T valueToRemove = list.get(index);
        int lastIndex = list.size() - 1;
        T lastValue = list.get(lastIndex);

        list.set(index, lastValue);
        list.remove(lastIndex);

        Set<Integer> valueIndices = indexMap.get(valueToRemove);
        valueIndices.remove(index);
        if (valueIndices.isEmpty()) {
            indexMap.remove(valueToRemove);
        }

        if (index != lastIndex) {
            Set<Integer> lastIndices = indexMap.get(lastValue);
            lastIndices.remove(lastIndex);
            lastIndices.add(index);
        }

        return true;
    }

    /**
     * Retorna l'element en la posició indicada.
     * 
     * @param   index Índex
     * @return  Element en la posició indicada
     * @throws IndexOutOfBoundsException si l'índex es invàlid
     */
    public T get(int index) {
        return list.get(index);
    }

    /**
     * Elimina tots els elements de la lista.
     */
    public void clear() {
        list.clear();
        indexMap.clear();
    }

    /**
     * Calcula el nombre d'elements de la lista.
     * 
     * @return La mida de la llista.
     */
    public int size() {
        return list.size();
    }

    /**
     * Verifica si la llista està buida.
     * 
     * @return  true si no hi ha elements, false en cas contrari.
     */
    public boolean isEmpty() {
        return list.isEmpty();
    }

    /**
     * Verifica si l'element del paràmetre està present.
     * 
     * @param value Element a comprobar.
     * @return      true si està, false en cas contrari.
     */
    public boolean contains(T value) {
        Set<Integer> indices = indexMap.get(value);
        return indices != null && !indices.isEmpty();
    }

    /**
     * Retorna una representació en text de la llista.
     * 
     * @return  String amb els elements actuals.
     */
    @Override
    public String toString() {
        return list.toString();
    }

    
    /**
     * Reconstrueix el mapa d'índexs a partir del contingut actual de la llista.
     * Cal cridar aquest mètode després de deserialitzar.
     */
    public void rebuildIndexMap() {
        indexMap = new HashMap<>();
        for (int i = 0; i < list.size(); ++i) {
            T value = list.get(i);
            indexMap.computeIfAbsent(value, k -> new HashSet<>()).add(i);
        }
    }
}