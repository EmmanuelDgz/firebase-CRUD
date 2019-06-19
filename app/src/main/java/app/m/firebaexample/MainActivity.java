package app.m.firebaexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText editTextName, editTextEmail, editTextNumber;
    Button buttonAddUser;
    ListView listViewUsers;

    List<Product> products;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();

        initListner();
    }


    private void findViews() {
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextNumber = (EditText) findViewById(R.id.editTextNumber);
        listViewUsers = (ListView) findViewById(R.id.listViewUsers);
        buttonAddUser = (Button) findViewById(R.id.buttonAddUser);

        products = new ArrayList<>();
    }

    private void initListner() {
        buttonAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUser();
            }
        });

        listViewUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Product Product = products.get(i);
                CallUpdateAndDeleteDialog(Product.getProductId(), Product.getProductName(), Product.getProductDescription(), Product.getProductPrice());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                products.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Product Product = postSnapshot.getValue(Product.class);

                    products.add(Product);
                }

                ProductList UserAdapter = new ProductList(MainActivity.this, products);

                listViewUsers.setAdapter(UserAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void CallUpdateAndDeleteDialog(final String userid, String username, final String email, String monumber) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.activity_update_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText updateTextname = (EditText) dialogView.findViewById(R.id.updateTextname);
        final EditText updateTextemail = (EditText) dialogView.findViewById(R.id.updateTextemail);
        final EditText updateTextmobileno = (EditText) dialogView.findViewById(R.id.updateTextmobileno);

        updateTextname.setText(username);
        updateTextemail.setText(email);
        updateTextmobileno.setText(monumber);

        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateUser);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteUser);

        dialogBuilder.setTitle(username);

        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = updateTextname.getText().toString().trim();
                String email = updateTextemail.getText().toString().trim();
                String mobilenumber = updateTextmobileno.getText().toString().trim();

                if (!TextUtils.isEmpty(name)) {
                    if (!TextUtils.isEmpty(email)) {
                        if (!TextUtils.isEmpty(mobilenumber)) {
                            updateUser(userid, name, email, mobilenumber);
                            b.dismiss();
                        }
                    }
                }

            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteUser(userid);
                b.dismiss();
            }
        });
    }

    private boolean updateUser(String id, String name, String email, String mobilenumber) {
        DatabaseReference UpdateReference = FirebaseDatabase.getInstance().getReference("users").child(id);
        Product Product = new Product(id, name, email, mobilenumber);

        UpdateReference.setValue(Product);

        Toast.makeText(getApplicationContext(), "Producto modificado", Toast.LENGTH_LONG).show();
        return true;
    }

    private boolean deleteUser(String id) {
        DatabaseReference DeleteReference = FirebaseDatabase.getInstance().getReference("users").child(id);

        DeleteReference.removeValue();

        Toast.makeText(getApplicationContext(), "Producto eliminado", Toast.LENGTH_LONG).show();
        return true;
    }


    private void addUser() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String mobilenumber = editTextNumber.getText().toString().trim();


        if (!TextUtils.isEmpty(name)) {
            if (!TextUtils.isEmpty(email)) {
                if (!TextUtils.isEmpty(mobilenumber)) {

                    String id = databaseReference.push().getKey();

                    Product Product = new Product(id, name, email, mobilenumber);

                    databaseReference.child(id).setValue(Product);

                    editTextName.setText("");
                    editTextNumber.setText("");
                    editTextEmail.setText("");

                    Toast.makeText(this, "Producto agregado", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Ingrese precio", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Ingrese descripción", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Ingrese nombre de producto", Toast.LENGTH_LONG).show();
        }
    }
}
