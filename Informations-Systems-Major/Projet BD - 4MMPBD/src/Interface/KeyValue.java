package Interface;

public class KeyValue {
    Integer key;
    String value;

    public KeyValue(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getValue() { return value; }
    public Integer getKey() { return key; }

    @Override
    public String toString() { return value; }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof KeyValue) {
            KeyValue kv = (KeyValue) obj;
            return (kv.value.equals(this.value));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.key.hashCode();
    }
}
