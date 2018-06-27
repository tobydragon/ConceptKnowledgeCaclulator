package edu.ithaca.dragon.tecmap.prediction;

class PredictionResult {

    private String studentId;
    private String expectedResult;
    private String predictedResult;
    private boolean correct;

    public PredictionResult(String id, String expected, String predicted) {
        studentId = id;
        expectedResult = expected;
        predictedResult = predicted;
        if (expectedResult.equals(predictedResult)) {
            correct = true;
        } else {
            correct = false;
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

    public boolean isCorrect() {
        return correct;
    }
}
