package database.entity;

import android.text.LoginFilter;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;


public class EmployeeEntity{
    private String id;
    private String Name;
    private String FirstName;
    private String Function;
    private String Telnumber;
    private String Email;
    private String Address;
    private String NPA;
    private String Image_Url;
    private String Username;
    private String Password;
    private Boolean isAdmin;

    public EmployeeEntity(){

    }

    public EmployeeEntity(String Name, String FirstName, String Function, String Telnumber, String Email, String Address, String NPA, String Image_Url, String Username, String Password, Boolean isAdmin){
        this.Name=Name;
        this.FirstName=FirstName;
        this.Function=Function;
        this.Telnumber=Telnumber;
        this.Email=Email;
        this.Address=Address;
        this.NPA=NPA;
        this.Image_Url=Image_Url;
        this.Username=Username;
        this.Password=Password;
        this.isAdmin=isAdmin;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getFunction() {
        return Function;
    }

    public void setFunction(String function) {
        Function = function;
    }

    public String getTelnumber() {
        return Telnumber;
    }

    public void setTelnumber(String telnumber) {
        Telnumber = telnumber;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getNPA() {
        return NPA;
    }

    public void setNPA(String NPA) {
        this.NPA = NPA;
    }

    public String getImage_Url() {
        return Image_Url;
    }

    public void setImage_Url(String image_Url) {
        Image_Url = image_Url;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }


    @Override
    public boolean equals(Object obj){
        if(obj==null) return false;
        if(obj==this) return true;
        if(!(obj instanceof EmployeeEntity)) return false;
        EmployeeEntity o = (EmployeeEntity)  obj;
        return  o.getId().equals(this.getId());
    }

    @Override
    public String toString(){
        return Name;
    }

    @Exclude
    public Map<String,Object> toMap()
    {
        HashMap<String,Object> result = new HashMap<>();

        result.put("name",Name);
        result.put("firstName",FirstName);
        result.put("function",Function);
        result.put("telNumber",Telnumber);
        result.put("email",Email);
        result.put("address",Address);
        result.put("NPA",NPA);
        result.put("image_url",Image_Url);
        result.put("username",Username);
        result.put("password",Password);
        result.put("isAdmin",isAdmin);

        return result;
    }
}








