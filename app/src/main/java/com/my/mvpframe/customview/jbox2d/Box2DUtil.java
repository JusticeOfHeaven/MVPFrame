package com.my.mvpframe.customview.jbox2d;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import static com.my.mvpframe.customview.jbox2d.Constant.RATE;

/**
 * Create by jzhan on 2018/12/3
 */
public class Box2DUtil {

    public static MyRectColor createBox(
            float x,
            float y,
            float halfWidth,
            float halfHeight,
            boolean isStatic,
            World world,
            int color) {

        FixtureDef def = new FixtureDef();

        if (isStatic) {
            def.density = 0;
        } else {
            def.density = 1.0f;
        }
        def.friction = 0.0f;
        def.restitution = 0.6f;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(halfWidth/RATE,halfHeight/RATE);
        def.shape = shape;

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(x/RATE,y/RATE);
        Body bodyTemp = world.createBody(bodyDef);
//        bodyTemp.createShape(shape);
//        bodyTemp.setMassFromShapes();

        return new MyRectColor(bodyTemp,halfWidth,halfHeight,color);
    }

//    public static MyCircleColor createCircle(
//            float x,
//            float y,
//            float radius,
//            World world,
//            int color) {
//        CircleShape shape = new CircleShape();
//        shape.
//    }
}
