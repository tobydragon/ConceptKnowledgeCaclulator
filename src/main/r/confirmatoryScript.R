#Additional Required Packages: semPlot, stringr

#Provide file paths. A modelFile can be generated through ConceptKnowledgeCalculator
csvFile = 
modelFile = 

#minCutoff - sets the absolute value cutoff for connections being plotted. Default is set to 0. Must be set to a value.
minCutoff = 0

confirmatoryFunction <- function(csvFile){
  
  datamatrix <- read.csv(file=csvFile, header = TRUE, sep = ",")
  datamatrix <- datamatrix[, -c(1,2)]
  datamatrix <- datamatrix[-c(2),]
  colnames(datamatrix)
  columnCount <- ncol(datamatrix)
  for(col in 1:columnCount){
    strArr <- unlist(strsplit(colnames(datamatrix)[col], "[.]"))
    lengthOfString <- length(strArr)
    strArr <- strArr[-c(lengthOfString)]
    cleanedStr <- do.call(paste, c(as.list(strArr), sep = ""))
    colnames(datamatrix)[col] <- cleanedStr
  }
  model.txt <- readLines(modelFile)
  model.dhp <- specifyModel(text=model.txt)
  dataCorrelation <- cor(datamatrix) 
  rowCount <- nrow(datamatrix)
  dataSem.dhp <- sem(model.dhp, dataCorrelation, rowCount)
  semPaths(dataSem.dhp, "est", minimum = minCutoff)
  return(dataSem.dhp)
}

confirmatoryData <- confirmatoryFunction(csvFile)
confirmatoryData