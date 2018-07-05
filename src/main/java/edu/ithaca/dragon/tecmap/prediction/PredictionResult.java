package edu.ithaca.dragon.tecmap.prediction;

class PredictionResult {

    private String studentId;
    private String expectedResult;
    private String predictedResult;
    private Result result;

    public PredictionResult(String id, String expected, String predicted) {
        studentId = id;
        expectedResult = expected;
        predictedResult = predicted;
        if (predictedResult.equals("AT-RISK")) {
            if (expectedResult.equals(predictedResult)) {
                //Predicted at risk and was a correct prediction
                result = Result.TRUE_POSITIVE;
            } else {
                //Predicted at risk, but was actually OK
                result = Result.FALSE_POSITIVE;
            }
        } else if (predictedResult.equals("OK")) {
            if (expectedResult.equals(predictedResult)) {
                //Predicted ok and was a correct prediction
                result = Result.TRUE_NEGATIVE;
            } else {
                //Predicted ok, but was actually at risk
                result = Result.FALSE_NEGATIVE;
            }
        }
    }

    public String getStudentId() {
        return studentId;
    }

    public String getExpectedResult() {
        return expectedResult;
    }

    public String getPredictedResult() {
        return predictedResult;
    }

    public Result getResult() {
        return result;
    }
}
