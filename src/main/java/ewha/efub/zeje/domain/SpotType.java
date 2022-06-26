package ewha.efub.zeje.domain;

public enum SpotType {
    A01("여행,자연"),
    A02("체험");

    final private String name;
    public String getName(){
        return name;
    }
    private SpotType(String name) {
        this.name = name;
    }
}
