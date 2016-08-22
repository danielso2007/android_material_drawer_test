package br.com.daniel.oliveira.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.io.ByteArrayOutputStream;

import br.com.daniel.oliveira.fragment.AjudaFragment;
import br.com.daniel.oliveira.fragment.ConfiguracaoFragment;
import br.com.daniel.oliveira.fragment.HomeFragment;
import br.com.daniel.oliveira.util.Utils;

public class MainActivity extends AppCompatActivity {

    public final static Integer REQUEST_CODE_INTENT = 16101;

    private FragmentManager fragmentManager;
    private AccountHeader accountHeader;
    private Drawer drawer;

    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().replace(R.id.container, new HomeFragment()).commit();

        // Encontrar o ponto de vista da barra de ferramentas dentro do layout da atividade
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolBarTop);
        // Define a barra de ferramentas para atuar como a Barra de ação para esta janela Atividade.
        // Certifique-se a barra de ferramentas existe na actividade e não é nulo
        setSupportActionBar(toolbar);

        //Se você deseja atualizar os itens em um momento posterior, recomenda-se mantê-lo em uma variável
        PrimaryDrawerItem item1 = new PrimaryDrawerItem()
                .withIdentifier(1)
                .withName(R.string.drawer_item_home)
                .withIcon(GoogleMaterial.Icon.gmd_dashboard);

//        SecondaryDrawerItem item2 = (SecondaryDrawerItem) new SecondaryDrawerItem()
//                .withIdentifier(2)
//                .withName(R.string.drawer_item_settings)
//                .withIcon(FontAwesome.Icon.faw_cogs);
        PrimaryDrawerItem item2 = (PrimaryDrawerItem) new SecondaryDrawerItem()
                .withIdentifier(2)
                .withName(R.string.drawer_item_settings)
                .withIcon(FontAwesome.Icon.faw_cog);

        PrimaryDrawerItem itemAjuda = new PrimaryDrawerItem()
                .withIdentifier(3)
                .withName(R.string.drawer_item_help)
                .withIcon(FontAwesome.Icon.faw_question_circle_o);

        PrimaryDrawerItem itemSair = new PrimaryDrawerItem()
                .withIdentifier(5)
                .withName(R.string.drawer_item_logout)
                .withIcon(FontAwesome.Icon.faw_sign_out);


        // Create the AccountHeader
        this.accountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        new ProfileDrawerItem().withName("Daniel Oliveira")
                                .withEmail("daniel@gmail.com")
                                .withIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.profile, null))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return Boolean.FALSE;
                    }
                }).withOnAccountHeaderProfileImageListener(new AccountHeader.OnAccountHeaderProfileImageListener() {
                    @Override
                    public boolean onProfileImageClick(View view, IProfile profile, boolean current) {

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, REQUEST_CODE_INTENT);

                        return Boolean.FALSE;
                    }

                    @Override
                    public boolean onProfileImageLongClick(View view, IProfile profile, boolean current) {
                        return Boolean.FALSE;
                    }
                })
                .build();

        //criar a gaveta e lembrar o objeto `resultado Drawer`
        this.drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(accountHeader)
                .withTranslucentStatusBar(Boolean.TRUE)
                .withActionBarDrawerToggle(Boolean.TRUE)
                .addDrawerItems(
                        item1,
                        item2,
                        itemAjuda,
                        new DividerDrawerItem(),
                        itemSair
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Log.i("TAG", String.valueOf(position));
                        switch (position) {
                            case 1:
                                toolbar.setTitle("Home");
                                fragmentManager.beginTransaction().replace(R.id.container, new HomeFragment()).commit();
                                break;
                            case 2:
                                toolbar.setTitle("Configuração");
                                fragmentManager.beginTransaction().replace(R.id.container, new ConfiguracaoFragment()).commit();
                                break;

                            case 3:
                                toolbar.setTitle("Ajuda");
                                fragmentManager.beginTransaction().replace(R.id.container, new AjudaFragment()).commit();
                                break;

                            case 5:
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                                builder.setMessage("Deseja sair da aplicação?");

                                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        finish();
                                    }
                                });

                                builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        dialog.cancel();
                                    }
                                });

                                AlertDialog alert= builder.create();
                                alert.show();

                                break;
                        }
                        return Boolean.FALSE;
                    }
                }).build();

        //Code - Show the back arrow:
//        result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(Boolean.FALSE);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(Boolean.TRUE);

        // Code - Show the hamburger icon:
        getSupportActionBar().setDisplayHomeAsUpEnabled(Boolean.FALSE);
        drawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(Boolean.TRUE);

//        //definir a seleção para o item com o identificador 1
//        drawer.setSelection(1);
//        //definir a seleção para o item com o identificador 2
//        drawer.setSelection(item2);
//        //definir a seleção e também disparar o `onItemClicklistener
//        drawer.setSelection(1, Boolean.TRUE);

        //modify an item of the drawer
        item1.withName("A new name for this drawerItem")
                .withBadge("19")
                .withBadgeStyle(new BadgeStyle()
                        .withTextColor(Color.WHITE)
                        .withColorRes(R.color.md_green_700));

        //notify the drawer about the updated element. it will take care about everything else
        drawer.updateItem(item1);

        //to update only the name, badge, icon you can also use one of the quick methods
        drawer.updateName(1, new StringHolder("Novo nome"));

        //O objeto resultado também permite adicionar novos itens, remover itens, adicionar rodapé, rodapé pegajoso, ..
//        result.addItem(new DividerDrawerItem());
//        result.addStickyFooterItem(new PrimaryDrawerItem().withName("StickyFooterItem").withSelectable(Boolean.FALSE));

        //remove items with an identifier
//        result.removeItem(4);

        //open / close the drawer
//        result.openDrawer();
//        result.closeDrawer();

        //get the reference to the `DrawerLayout` itself
//        result.getDrawerLayout();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_INTENT && resultCode == RESULT_OK){

            //Imagem que veio da câmera
            Bundle extras = data.getExtras();
            Bitmap imagem = (Bitmap) extras.get("data");

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imagem.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            atualizarProfile(byteArray);
        }
    }

    private void atualizarProfile(byte[] byteArray) {
        accountHeader.removeProfile(0);

        Bitmap imagem = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        ProfileDrawerItem profile = new ProfileDrawerItem()
                .withName("Daniel Oliveira")
                .withEmail("daniel@gmail.com")
                .withIcon(imagem);

        accountHeader.addProfiles(profile);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Utils.showToastShort(this, "Precione mais uma vez para sair");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

}
