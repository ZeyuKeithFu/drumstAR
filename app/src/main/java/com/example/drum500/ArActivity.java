package com.example.drum500;

import android.graphics.PointF;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;

public class ArActivity extends AppCompatActivity {

    Button back;
    ArFragment arFragment;
    ModelRenderable drumRenderable;
    ModelRenderable cubeRenderable;
    QRCodeReaderView qrCodeReaderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar);
        // Hide top bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        // Set UI elements
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);
        back = (Button) findViewById(R.id.backButton);
        //qrCodeReaderView = (QRCodeReaderView) findViewById(R.id.qrcodeView);

        // Set back function
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Scan QR code
//        qrCodeReaderView.setOnQRCodeReadListener(new QRCodeReaderView.OnQRCodeReadListener() {
//            @Override
//            public void onQRCodeRead(String text, PointF[] points) {
//                Log.i("Info", "Code read");
//            }
//        });

        // Set renderable shape
        MaterialFactory.makeOpaqueWithColor(this, new Color(android.graphics.Color.DKGRAY))
                .thenAccept(
                        material -> {
                            cubeRenderable = ShapeFactory.makeCube(
                                    new Vector3(0.1f, 0.1f, 0.1f),
                                    new Vector3(0.0f, 0.15f, 0.0f),
                                    material);
                        }
                );

        // Set renderable drum
        ModelRenderable.builder()
                // To load as an asset from the 'assets' folder ('src/main/assets/andy.sfb'):
                .setSource(this, Uri.parse("10377_KettleDrum.sfb"))

                // Instead, load as a resource from the 'res/raw' folder ('src/main/res/raw/andy.sfb'):
                //.setSource(this, R.raw.andy)

                .build()
                .thenAccept(renderable -> drumRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Log.e("Info", "Unable to load Renderable.", throwable);
                            return null;
                        });

        // Set on plane tap
        arFragment.setOnTapArPlaneListener(new BaseArFragment.OnTapArPlaneListener() {
            @Override
            public void onTapPlane(HitResult hitResult, Plane plane, MotionEvent motionEvent) {
                Log.i("Info", "Plain tapped");
                // Create the Anchor
                Anchor anchor = hitResult.createAnchor();
                AnchorNode anchorNode = new AnchorNode(anchor);
                anchorNode.setParent(arFragment.getArSceneView().getScene());

                Node drumPad = new Node();
                drumPad.setParent(anchorNode);
                drumPad.setRenderable(drumRenderable);
            }

        });


    }
}
