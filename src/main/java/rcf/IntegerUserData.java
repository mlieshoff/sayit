package rcf;

public class IntegerUserData extends UserData<Integer> {

    @Override
    public int compareTo(UserData<Integer> o) {
        return Integer.compare(getValue(), o.getValue());
    }

}
