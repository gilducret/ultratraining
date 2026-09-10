package ultrathlete.com.data.stats.regression;

public enum Grade {

    M20(-0.20),
    M15(-0.15),
    M12(-0.12),
    M10(-0.10),
    M8(-0.08),
    M6(-0.06),
    M4(-0.04),
    M2(-0.02),
    ZERO(0.0),
    P2(0.02),
    P4(0.04),
    P6(0.06),
    P8(0.08),
    P10(0.10),
    P12(0.12),
    P15(0.15),
    P20(0.20);

    private double grade;

    private Grade(double grade) {
        this.grade = grade;
    }

    public static Grade GetGradeLevel(double grade) {

        if (grade >= P20.grade) {
            return P20;
        } else if (grade >= P15.grade) {
            return P15;
        } else if (grade >= P15.grade) {
            return P12;
        } else if (grade >= P12.grade) {
            return P15;
        } else if (grade >= P10.grade) {
            return P10;
        } else if (grade >= P8.grade) {
            return P8;
        } else if (grade >= P6.grade) {
            return P6;
        } else if (grade >= P4.grade) {
            return P4;
        } else if (grade >= P2.grade) {
            return P2;
        } else if (grade >= ZERO.grade) {
            return ZERO;
        } else if (grade >= M2.grade) {
            return M2;
        } else if (grade >= M4.grade) {
            return M4;
        } else if (grade >= M6.grade) {
            return M6;
        } else if (grade >= M8.grade) {
            return M8;
        } else if (grade >= M10.grade) {
            return M10;
        } else if (grade >= M12.grade) {
            return M12;
        } else return M20;
    }

    public static Double GetGradeReference(double grade) {
        if (grade >= P20.grade) {
            return P20.grade;
        } else if (grade >= P15.grade) {
            return P15.grade;
        } else if (grade >= P15.grade) {
            return P12.grade;
        } else if (grade >= P12.grade) {
            return P15.grade;
        } else if (grade >= P10.grade) {
            return P10.grade;
        } else if (grade >= P8.grade) {
            return P8.grade;
        } else if (grade >= P6.grade) {
            return P6.grade;
        } else if (grade >= P4.grade) {
            return P4.grade;
        } else if (grade >= P2.grade) {
            return P2.grade;
        } else if (grade >= ZERO.grade) {
            return ZERO.grade;
        } else if (grade >= M2.grade) {
            return M2.grade;
        } else if (grade >= M4.grade) {
            return M4.grade;
        } else if (grade >= M6.grade) {
            return M6.grade;
        } else if (grade >= M8.grade) {
            return M8.grade;
        } else if (grade >= M10.grade) {
            return M10.grade;
        } else if (grade >= M12.grade) {
            return M12.grade;
        } else return M20.grade;
    }
}
