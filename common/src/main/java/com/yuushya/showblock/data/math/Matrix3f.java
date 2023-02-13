package com.yuushya.showblock.data.math;

import com.mojang.datafixers.util.Pair;
import net.minecraft.util.Mth;
import org.apache.commons.lang3.tuple.Triple;

import java.nio.FloatBuffer;

public class Matrix3f {
    private static final int ORDER = 3;
    private static final float G = 3.0F + 2.0F * (float) Math.sqrt(2.0);
    private static final float CS = (float) Math.cos(0.39269908169872414);
    private static final float SS = (float) Math.sin(0.39269908169872414);
    private static final float SQ2 = 1.0F / (float) Math.sqrt(2.0);
    protected float m00;
    protected float m01;
    protected float m02;
    protected float m10;
    protected float m11;
    protected float m12;
    protected float m20;
    protected float m21;
    protected float m22;

    public Matrix3f() {
    }

    public Matrix3f(Quaternion quaternion) {
        float f = quaternion.i();
        float g = quaternion.j();
        float h = quaternion.k();
        float i = quaternion.r();
        float j = 2.0F * f * f;
        float k = 2.0F * g * g;
        float l = 2.0F * h * h;
        this.m00 = 1.0F - k - l;
        this.m11 = 1.0F - l - j;
        this.m22 = 1.0F - j - k;
        float m = f * g;
        float n = g * h;
        float o = h * f;
        float p = f * i;
        float q = g * i;
        float r = h * i;
        this.m10 = 2.0F * (m + r);
        this.m01 = 2.0F * (m - r);
        this.m20 = 2.0F * (o - q);
        this.m02 = 2.0F * (o + q);
        this.m21 = 2.0F * (n + p);
        this.m12 = 2.0F * (n - p);
    }

    public static Matrix3f createScaleMatrix(float f, float g, float h) {
        Matrix3f matrix3f = new Matrix3f();
        matrix3f.m00 = f;
        matrix3f.m11 = g;
        matrix3f.m22 = h;
        return matrix3f;
    }

    public Matrix3f(Matrix4f matrix4f) {
        this.m00 = matrix4f.m00;
        this.m01 = matrix4f.m01;
        this.m02 = matrix4f.m02;
        this.m10 = matrix4f.m10;
        this.m11 = matrix4f.m11;
        this.m12 = matrix4f.m12;
        this.m20 = matrix4f.m20;
        this.m21 = matrix4f.m21;
        this.m22 = matrix4f.m22;
    }

    public Matrix3f(Matrix3f matrix3f) {
        this.m00 = matrix3f.m00;
        this.m01 = matrix3f.m01;
        this.m02 = matrix3f.m02;
        this.m10 = matrix3f.m10;
        this.m11 = matrix3f.m11;
        this.m12 = matrix3f.m12;
        this.m20 = matrix3f.m20;
        this.m21 = matrix3f.m21;
        this.m22 = matrix3f.m22;
    }

    private static Pair<Float, Float> approxGivensQuat(float f, float g, float h) {
        float i = 2.0F * (f - h);
        if (G * g * g < i * i) {
            float k = Mth.fastInvSqrt(g * g + i * i);
            return Pair.of(k * g, k * i);
        } else {
            return Pair.of(SS, CS);
        }
    }

    private static Pair<Float, Float> qrGivensQuat(float f, float g) {
        float h = (float) Math.hypot(f, g);
        float i = h > 1.0E-6F ? g : 0.0F;
        float j = Math.abs(f) + Math.max(h, 1.0E-6F);
        float k;
        if (f < 0.0F) {
            k = i;
            i = j;
            j = k;
        }

        k = Mth.fastInvSqrt(j * j + i * i);
        j *= k;
        i *= k;
        return Pair.of(i, j);
    }

    private static Quaternion stepJacobi(Matrix3f matrix3f) {
        Matrix3f matrix3f2 = new Matrix3f();
        Quaternion quaternion = Quaternion.ONE.copy();
        Pair pair;
        Float float_;
        Float float2;
        Quaternion quaternion2;
        float f;
        float g;
        float h;
        if (matrix3f.m01 * matrix3f.m01 + matrix3f.m10 * matrix3f.m10 > 1.0E-6F) {
            pair = approxGivensQuat(matrix3f.m00, 0.5F * (matrix3f.m01 + matrix3f.m10), matrix3f.m11);
            float_ = (Float) pair.getFirst();
            float2 = (Float) pair.getSecond();
            quaternion2 = new Quaternion(0.0F, 0.0F, float_, float2);
            f = float2 * float2 - float_ * float_;
            g = -2.0F * float_ * float2;
            h = float2 * float2 + float_ * float_;
            quaternion.mul(quaternion2);
            matrix3f2.setIdentity();
            matrix3f2.m00 = f;
            matrix3f2.m11 = f;
            matrix3f2.m10 = -g;
            matrix3f2.m01 = g;
            matrix3f2.m22 = h;
            matrix3f.mul(matrix3f2);
            matrix3f2.transpose();
            matrix3f2.mul(matrix3f);
            matrix3f.load(matrix3f2);
        }

        if (matrix3f.m02 * matrix3f.m02 + matrix3f.m20 * matrix3f.m20 > 1.0E-6F) {
            pair = approxGivensQuat(matrix3f.m00, 0.5F * (matrix3f.m02 + matrix3f.m20), matrix3f.m22);
            float i = -(Float) pair.getFirst();
            float2 = (Float) pair.getSecond();
            quaternion2 = new Quaternion(0.0F, i, 0.0F, float2);
            f = float2 * float2 - i * i;
            g = -2.0F * i * float2;
            h = float2 * float2 + i * i;
            quaternion.mul(quaternion2);
            matrix3f2.setIdentity();
            matrix3f2.m00 = f;
            matrix3f2.m22 = f;
            matrix3f2.m20 = g;
            matrix3f2.m02 = -g;
            matrix3f2.m11 = h;
            matrix3f.mul(matrix3f2);
            matrix3f2.transpose();
            matrix3f2.mul(matrix3f);
            matrix3f.load(matrix3f2);
        }

        if (matrix3f.m12 * matrix3f.m12 + matrix3f.m21 * matrix3f.m21 > 1.0E-6F) {
            pair = approxGivensQuat(matrix3f.m11, 0.5F * (matrix3f.m12 + matrix3f.m21), matrix3f.m22);
            float_ = (Float) pair.getFirst();
            float2 = (Float) pair.getSecond();
            quaternion2 = new Quaternion(float_, 0.0F, 0.0F, float2);
            f = float2 * float2 - float_ * float_;
            g = -2.0F * float_ * float2;
            h = float2 * float2 + float_ * float_;
            quaternion.mul(quaternion2);
            matrix3f2.setIdentity();
            matrix3f2.m11 = f;
            matrix3f2.m22 = f;
            matrix3f2.m21 = -g;
            matrix3f2.m12 = g;
            matrix3f2.m00 = h;
            matrix3f.mul(matrix3f2);
            matrix3f2.transpose();
            matrix3f2.mul(matrix3f);
            matrix3f.load(matrix3f2);
        }

        return quaternion;
    }

    private static void sortSingularValues(Matrix3f matrix3f, Quaternion quaternion) {
        float f = matrix3f.m00 * matrix3f.m00 + matrix3f.m10 * matrix3f.m10 + matrix3f.m20 * matrix3f.m20;
        float g = matrix3f.m01 * matrix3f.m01 + matrix3f.m11 * matrix3f.m11 + matrix3f.m21 * matrix3f.m21;
        float h = matrix3f.m02 * matrix3f.m02 + matrix3f.m12 * matrix3f.m12 + matrix3f.m22 * matrix3f.m22;
        float i;
        Quaternion quaternion2;
        if (f < g) {
            i = matrix3f.m10;
            matrix3f.m10 = -matrix3f.m00;
            matrix3f.m00 = i;
            i = matrix3f.m11;
            matrix3f.m11 = -matrix3f.m01;
            matrix3f.m01 = i;
            i = matrix3f.m12;
            matrix3f.m12 = -matrix3f.m02;
            matrix3f.m02 = i;
            quaternion2 = new Quaternion(0.0F, 0.0F, SQ2, SQ2);
            quaternion.mul(quaternion2);
            i = f;
            f = g;
            g = i;
        }

        if (f < h) {
            i = matrix3f.m20;
            matrix3f.m20 = -matrix3f.m00;
            matrix3f.m00 = i;
            i = matrix3f.m21;
            matrix3f.m21 = -matrix3f.m01;
            matrix3f.m01 = i;
            i = matrix3f.m22;
            matrix3f.m22 = -matrix3f.m02;
            matrix3f.m02 = i;
            quaternion2 = new Quaternion(0.0F, SQ2, 0.0F, SQ2);
            quaternion.mul(quaternion2);
            h = f;
        }

        if (g < h) {
            i = matrix3f.m20;
            matrix3f.m20 = -matrix3f.m10;
            matrix3f.m10 = i;
            i = matrix3f.m21;
            matrix3f.m21 = -matrix3f.m11;
            matrix3f.m11 = i;
            i = matrix3f.m22;
            matrix3f.m22 = -matrix3f.m12;
            matrix3f.m12 = i;
            quaternion2 = new Quaternion(SQ2, 0.0F, 0.0F, SQ2);
            quaternion.mul(quaternion2);
        }

    }

    public void transpose() {
        float f = this.m01;
        this.m01 = this.m10;
        this.m10 = f;
        f = this.m02;
        this.m02 = this.m20;
        this.m20 = f;
        f = this.m12;
        this.m12 = this.m21;
        this.m21 = f;
    }

    public Triple<Quaternion, Vector3f, Quaternion> svdDecompose() {
        Quaternion quaternion = Quaternion.ONE.copy();
        Quaternion quaternion2 = Quaternion.ONE.copy();
        Matrix3f matrix3f = this.copy();
        matrix3f.transpose();
        matrix3f.mul(this);

        for (int i = 0; i < 5; ++i) {
            quaternion2.mul(stepJacobi(matrix3f));
        }

        quaternion2.normalize();
        Matrix3f matrix3f2 = new Matrix3f(this);
        matrix3f2.mul(new Matrix3f(quaternion2));
        float f = 1.0F;
        Pair<Float, Float> pair = qrGivensQuat(matrix3f2.m00, matrix3f2.m10);
        Float float_ = pair.getFirst();
        Float float2 = pair.getSecond();
        float g = float2 * float2 - float_ * float_;
        float h = -2.0F * float_ * float2;
        float j = float2 * float2 + float_ * float_;
        Quaternion quaternion3 = new Quaternion(0.0F, 0.0F, float_, float2);
        quaternion.mul(quaternion3);
        Matrix3f matrix3f3 = new Matrix3f();
        matrix3f3.setIdentity();
        matrix3f3.m00 = g;
        matrix3f3.m11 = g;
        matrix3f3.m10 = h;
        matrix3f3.m01 = -h;
        matrix3f3.m22 = j;
        f *= j;
        matrix3f3.mul(matrix3f2);
        pair = qrGivensQuat(matrix3f3.m00, matrix3f3.m20);
        float k = -(Float) pair.getFirst();
        Float float3 = pair.getSecond();
        float l = float3 * float3 - k * k;
        float m = -2.0F * k * float3;
        float n = float3 * float3 + k * k;
        Quaternion quaternion4 = new Quaternion(0.0F, k, 0.0F, float3);
        quaternion.mul(quaternion4);
        Matrix3f matrix3f4 = new Matrix3f();
        matrix3f4.setIdentity();
        matrix3f4.m00 = l;
        matrix3f4.m22 = l;
        matrix3f4.m20 = -m;
        matrix3f4.m02 = m;
        matrix3f4.m11 = n;
        f *= n;
        matrix3f4.mul(matrix3f3);
        pair = qrGivensQuat(matrix3f4.m11, matrix3f4.m21);
        Float float4 = pair.getFirst();
        Float float5 = pair.getSecond();
        float o = float5 * float5 - float4 * float4;
        float p = -2.0F * float4 * float5;
        float q = float5 * float5 + float4 * float4;
        Quaternion quaternion5 = new Quaternion(float4, 0.0F, 0.0F, float5);
        quaternion.mul(quaternion5);
        Matrix3f matrix3f5 = new Matrix3f();
        matrix3f5.setIdentity();
        matrix3f5.m11 = o;
        matrix3f5.m22 = o;
        matrix3f5.m21 = p;
        matrix3f5.m12 = -p;
        matrix3f5.m00 = q;
        f *= q;
        matrix3f5.mul(matrix3f4);
        f = 1.0F / f;
        quaternion.mul((float) Math.sqrt(f));
        Vector3f vector3f = new Vector3f(matrix3f5.m00 * f, matrix3f5.m11 * f, matrix3f5.m22 * f);
        return Triple.of(quaternion, vector3f, quaternion2);
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object != null && this.getClass() == object.getClass()) {
            Matrix3f matrix3f = (Matrix3f) object;
            return Float.compare(matrix3f.m00, this.m00) == 0 && Float.compare(matrix3f.m01, this.m01) == 0 && Float.compare(matrix3f.m02, this.m02) == 0 && Float.compare(matrix3f.m10, this.m10) == 0 && Float.compare(matrix3f.m11, this.m11) == 0 && Float.compare(matrix3f.m12, this.m12) == 0 && Float.compare(matrix3f.m20, this.m20) == 0 && Float.compare(matrix3f.m21, this.m21) == 0 && Float.compare(matrix3f.m22, this.m22) == 0;
        } else {
            return false;
        }
    }

    public int hashCode() {
        int i = this.m00 != 0.0F ? Float.floatToIntBits(this.m00) : 0;
        i = 31 * i + (this.m01 != 0.0F ? Float.floatToIntBits(this.m01) : 0);
        i = 31 * i + (this.m02 != 0.0F ? Float.floatToIntBits(this.m02) : 0);
        i = 31 * i + (this.m10 != 0.0F ? Float.floatToIntBits(this.m10) : 0);
        i = 31 * i + (this.m11 != 0.0F ? Float.floatToIntBits(this.m11) : 0);
        i = 31 * i + (this.m12 != 0.0F ? Float.floatToIntBits(this.m12) : 0);
        i = 31 * i + (this.m20 != 0.0F ? Float.floatToIntBits(this.m20) : 0);
        i = 31 * i + (this.m21 != 0.0F ? Float.floatToIntBits(this.m21) : 0);
        i = 31 * i + (this.m22 != 0.0F ? Float.floatToIntBits(this.m22) : 0);
        return i;
    }

    private static int bufferIndex(int i, int j) {
        return j * 3 + i;
    }

    public void load(FloatBuffer floatBuffer) {
        this.m00 = floatBuffer.get(bufferIndex(0, 0));
        this.m01 = floatBuffer.get(bufferIndex(0, 1));
        this.m02 = floatBuffer.get(bufferIndex(0, 2));
        this.m10 = floatBuffer.get(bufferIndex(1, 0));
        this.m11 = floatBuffer.get(bufferIndex(1, 1));
        this.m12 = floatBuffer.get(bufferIndex(1, 2));
        this.m20 = floatBuffer.get(bufferIndex(2, 0));
        this.m21 = floatBuffer.get(bufferIndex(2, 1));
        this.m22 = floatBuffer.get(bufferIndex(2, 2));
    }

    public void loadTransposed(FloatBuffer floatBuffer) {
        this.m00 = floatBuffer.get(bufferIndex(0, 0));
        this.m01 = floatBuffer.get(bufferIndex(1, 0));
        this.m02 = floatBuffer.get(bufferIndex(2, 0));
        this.m10 = floatBuffer.get(bufferIndex(0, 1));
        this.m11 = floatBuffer.get(bufferIndex(1, 1));
        this.m12 = floatBuffer.get(bufferIndex(2, 1));
        this.m20 = floatBuffer.get(bufferIndex(0, 2));
        this.m21 = floatBuffer.get(bufferIndex(1, 2));
        this.m22 = floatBuffer.get(bufferIndex(2, 2));
    }

    public void load(FloatBuffer floatBuffer, boolean bl) {
        if (bl) {
            this.loadTransposed(floatBuffer);
        } else {
            this.load(floatBuffer);
        }

    }

    public void load(Matrix3f matrix3f) {
        this.m00 = matrix3f.m00;
        this.m01 = matrix3f.m01;
        this.m02 = matrix3f.m02;
        this.m10 = matrix3f.m10;
        this.m11 = matrix3f.m11;
        this.m12 = matrix3f.m12;
        this.m20 = matrix3f.m20;
        this.m21 = matrix3f.m21;
        this.m22 = matrix3f.m22;
    }

    public String toString() {
        return "Matrix3f:\n" +
                this.m00 +
                " " +
                this.m01 +
                " " +
                this.m02 +
                "\n" +
                this.m10 +
                " " +
                this.m11 +
                " " +
                this.m12 +
                "\n" +
                this.m20 +
                " " +
                this.m21 +
                " " +
                this.m22 +
                "\n";
    }

    public void store(FloatBuffer floatBuffer) {
        floatBuffer.put(bufferIndex(0, 0), this.m00);
        floatBuffer.put(bufferIndex(0, 1), this.m01);
        floatBuffer.put(bufferIndex(0, 2), this.m02);
        floatBuffer.put(bufferIndex(1, 0), this.m10);
        floatBuffer.put(bufferIndex(1, 1), this.m11);
        floatBuffer.put(bufferIndex(1, 2), this.m12);
        floatBuffer.put(bufferIndex(2, 0), this.m20);
        floatBuffer.put(bufferIndex(2, 1), this.m21);
        floatBuffer.put(bufferIndex(2, 2), this.m22);
    }

    public void storeTransposed(FloatBuffer floatBuffer) {
        floatBuffer.put(bufferIndex(0, 0), this.m00);
        floatBuffer.put(bufferIndex(1, 0), this.m01);
        floatBuffer.put(bufferIndex(2, 0), this.m02);
        floatBuffer.put(bufferIndex(0, 1), this.m10);
        floatBuffer.put(bufferIndex(1, 1), this.m11);
        floatBuffer.put(bufferIndex(2, 1), this.m12);
        floatBuffer.put(bufferIndex(0, 2), this.m20);
        floatBuffer.put(bufferIndex(1, 2), this.m21);
        floatBuffer.put(bufferIndex(2, 2), this.m22);
    }

    public void store(FloatBuffer floatBuffer, boolean bl) {
        if (bl) {
            this.storeTransposed(floatBuffer);
        } else {
            this.store(floatBuffer);
        }

    }

    public void setIdentity() {
        this.m00 = 1.0F;
        this.m01 = 0.0F;
        this.m02 = 0.0F;
        this.m10 = 0.0F;
        this.m11 = 1.0F;
        this.m12 = 0.0F;
        this.m20 = 0.0F;
        this.m21 = 0.0F;
        this.m22 = 1.0F;
    }

    public float adjugateAndDet() {
        float f = this.m11 * this.m22 - this.m12 * this.m21;
        float g = -(this.m10 * this.m22 - this.m12 * this.m20);
        float h = this.m10 * this.m21 - this.m11 * this.m20;
        float i = -(this.m01 * this.m22 - this.m02 * this.m21);
        float j = this.m00 * this.m22 - this.m02 * this.m20;
        float k = -(this.m00 * this.m21 - this.m01 * this.m20);
        float l = this.m01 * this.m12 - this.m02 * this.m11;
        float m = -(this.m00 * this.m12 - this.m02 * this.m10);
        float n = this.m00 * this.m11 - this.m01 * this.m10;
        float o = this.m00 * f + this.m01 * g + this.m02 * h;
        this.m00 = f;
        this.m10 = g;
        this.m20 = h;
        this.m01 = i;
        this.m11 = j;
        this.m21 = k;
        this.m02 = l;
        this.m12 = m;
        this.m22 = n;
        return o;
    }

    public float determinant() {
        float f = this.m11 * this.m22 - this.m12 * this.m21;
        float g = -(this.m10 * this.m22 - this.m12 * this.m20);
        float h = this.m10 * this.m21 - this.m11 * this.m20;
        return this.m00 * f + this.m01 * g + this.m02 * h;
    }

    public boolean invert() {
        float f = this.adjugateAndDet();
        if (Math.abs(f) > 1.0E-6F) {
            this.mul(f);
            return true;
        } else {
            return false;
        }
    }

    public void set(int i, int j, float f) {
        if (i == 0) {
            if (j == 0) {
                this.m00 = f;
            } else if (j == 1) {
                this.m01 = f;
            } else {
                this.m02 = f;
            }
        } else if (i == 1) {
            if (j == 0) {
                this.m10 = f;
            } else if (j == 1) {
                this.m11 = f;
            } else {
                this.m12 = f;
            }
        } else if (j == 0) {
            this.m20 = f;
        } else if (j == 1) {
            this.m21 = f;
        } else {
            this.m22 = f;
        }

    }

    public void mul(Matrix3f matrix3f) {
        float f = this.m00 * matrix3f.m00 + this.m01 * matrix3f.m10 + this.m02 * matrix3f.m20;
        float g = this.m00 * matrix3f.m01 + this.m01 * matrix3f.m11 + this.m02 * matrix3f.m21;
        float h = this.m00 * matrix3f.m02 + this.m01 * matrix3f.m12 + this.m02 * matrix3f.m22;
        float i = this.m10 * matrix3f.m00 + this.m11 * matrix3f.m10 + this.m12 * matrix3f.m20;
        float j = this.m10 * matrix3f.m01 + this.m11 * matrix3f.m11 + this.m12 * matrix3f.m21;
        float k = this.m10 * matrix3f.m02 + this.m11 * matrix3f.m12 + this.m12 * matrix3f.m22;
        float l = this.m20 * matrix3f.m00 + this.m21 * matrix3f.m10 + this.m22 * matrix3f.m20;
        float m = this.m20 * matrix3f.m01 + this.m21 * matrix3f.m11 + this.m22 * matrix3f.m21;
        float n = this.m20 * matrix3f.m02 + this.m21 * matrix3f.m12 + this.m22 * matrix3f.m22;
        this.m00 = f;
        this.m01 = g;
        this.m02 = h;
        this.m10 = i;
        this.m11 = j;
        this.m12 = k;
        this.m20 = l;
        this.m21 = m;
        this.m22 = n;
    }

    public void mul(Quaternion quaternion) {
        this.mul(new Matrix3f(quaternion));
    }

    public void mul(float f) {
        this.m00 *= f;
        this.m01 *= f;
        this.m02 *= f;
        this.m10 *= f;
        this.m11 *= f;
        this.m12 *= f;
        this.m20 *= f;
        this.m21 *= f;
        this.m22 *= f;
    }

    public void add(Matrix3f matrix3f) {
        this.m00 += matrix3f.m00;
        this.m01 += matrix3f.m01;
        this.m02 += matrix3f.m02;
        this.m10 += matrix3f.m10;
        this.m11 += matrix3f.m11;
        this.m12 += matrix3f.m12;
        this.m20 += matrix3f.m20;
        this.m21 += matrix3f.m21;
        this.m22 += matrix3f.m22;
    }

    public void sub(Matrix3f matrix3f) {
        this.m00 -= matrix3f.m00;
        this.m01 -= matrix3f.m01;
        this.m02 -= matrix3f.m02;
        this.m10 -= matrix3f.m10;
        this.m11 -= matrix3f.m11;
        this.m12 -= matrix3f.m12;
        this.m20 -= matrix3f.m20;
        this.m21 -= matrix3f.m21;
        this.m22 -= matrix3f.m22;
    }

    public float trace() {
        return this.m00 + this.m11 + this.m22;
    }

    public Matrix3f copy() {
        return new Matrix3f(this);
    }
}