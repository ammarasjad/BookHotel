package ammar.com.bookhotel.Object;


public class cart_object {
    private String kids;
    private String adult;
    private String room_quantity;
    private String room;
    private String hotel_name;
    private int descount_percent = 0;
    private int payment_value = 0;

    public int getDescount_percent() {
        return descount_percent;
    }

    public void setDescount_percent(int descount_percent) {
        this.descount_percent = descount_percent;
    }

    public int getPayment_value() {
        return payment_value;
    }

    public void setPayment_value(int payment_value) {
        this.payment_value = payment_value;
    }

    public int getHotel_no() {
        return hotel_no;
    }

    public void setHotel_no(int hotel_no) {
        this.hotel_no = hotel_no;
    }

    private int hotel_no;

    public String getKids() {
        return kids;
    }

    public void setKids(String kids) {
        this.kids = kids;
    }

    public String getAdult() {
        return adult;
    }

    public void setAdult(String adult) {
        this.adult = adult;
    }

    public String getRoom_quantity() {
        return room_quantity;
    }

    public void setRoom_quantity(String room_quantity) {
        this.room_quantity = room_quantity;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getHotel_name() {
        return hotel_name;
    }

    public void setHotel_name(String hotel_name) {
        this.hotel_name = hotel_name;
    }

    public int getSet_hotel_img() {
        return set_hotel_img;
    }

    public void setSet_hotel_img(int set_hotel_img) {
        this.set_hotel_img = set_hotel_img;
    }

    private int set_hotel_img;
}
