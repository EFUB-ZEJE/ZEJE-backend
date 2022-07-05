package ewha.efub.zeje.domain;

public enum SpotType {
    A01("자연"),
    A0202("힐링"),
    A02020700("공원"),
    A02020600("테마공원"),
    A02020200("관광단지"),
    A02030400("이색"),
    A02030100("농산어촌"),
    A02030500("관광농원"),
    A02030600("이색"),
    ;
    final private String name;
    public String getName(){
        return name;
    }
    private SpotType(String name) {
        this.name = name;
    }
}
