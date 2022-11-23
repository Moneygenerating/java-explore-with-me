package ewm.event.model;

//Список состояний жизненного цикла события
public enum StateLifecycle {
    //в ожидании
    PENDING,
    //опубликован
    PUBLISHED,
    //отменен
    CANCELED;

    public static StateLifecycle from(String state) {
        for (StateLifecycle value : StateLifecycle.values()) {
            if (value.name().equals(state)) {
                return value;
            }
        }
        return null;
    }
}
