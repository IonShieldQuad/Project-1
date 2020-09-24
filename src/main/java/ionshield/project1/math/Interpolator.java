package ionshield.project1.math;

public interface Interpolator {
    double lower();
    double upper();
    double lowerVal();
    double upperVal();
    double evaluate(double value) throws InterpolationException;
}
