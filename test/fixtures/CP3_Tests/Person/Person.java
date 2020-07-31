class Person {
    int id;
    int age;

    Person() {}

    Person(int id, int age) {
        this.id = id;
        this.age = age;
    }

    public int getId() {
        return this.id;
    }

    public int getAge() {
        return this.age;
    }

    public void setId(int var1) {
        this.id = var1;
    }

    public void setAge(int var1) {
        this.age = var1;
    }

    public void printInfo() {
        System.out.println("A pessoa com o id " + this.id + " tem " + this.age + " anos.");
    }
}
