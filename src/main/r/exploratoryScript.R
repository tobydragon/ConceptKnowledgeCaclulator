#Additional Required Packages: semPlot

#Provide file path
csvFile =

#minCutoff - sets the absolute value cutoff for connections being plotted. Default is set to 0. Must be set to a value.
minCutoff = 0

#Filters out columns with 0 variance in data and recounts the number of columns  
findColumnCount <- function(datamatrix){
  
  columnCount <- ncol(datamatrix)
  
  #delete any columns that have no variance in data
  deleteVector = c()
  vectorIndex <- 0
  for(i in columnCount:1){
    if(datamatrix[1,i] == mean(datamatrix[,i])){
      vectorIndex <- vectorIndex + 1
      deleteVector[vectorIndex] <- names(datamatrix[i])
      datamatrix <- matrix[,-c(i)]
    }
  }
  columnCount <- ncol(datamatrix)
  return(columnCount)
}  

matrixCleaner <- function(datamatrix){
  
  columnCount <- ncol(datamatrix)
  
  #delete any columns that have no variance in data
  deleteVector = c()
  vectorIndex <- 0
  for(i in columnCount:1){
    if(datamatrix[1,i] == mean(datamatrix[,i])){
      vectorIndex <- vectorIndex + 1
      deleteVector[vectorIndex] <- names(datamatrix[i])
      datamatrix <- datamatrix[,-c(i)]
    }
  }
  if(vectorIndex > 0){
    print("The LearningObjects directly below were removed due to 0 variance throughout all responses:");
    print(deleteVector)
  }
  return(datamatrix)
}
  
#Finds the correct number of factors that should be used in calculating the exploratory data
findFactorCount <- function(datamatrix){
  
  numOfFactors = 0
  columnCount <- findColumnCount(datamatrix)
  if(columnCount > 2){
    #sets a limit on how high R will go to find the factor amount based on how man columns exist
    if(columnCount %% 2 == 0){
      upperlimit <- (columnCount/2) - 1
    }else{
      upperlimit <- (columnCount/2) - 0.5
    }
    vector = c()
    for(i in 1:upperlimit){
      vector[i] <- factanal(datamatrix, i, method = "mle")$PVAL
    }
    for(i in 1:upperlimit){
      if(vector[i] < .05){
        numOfFactors <- i + 1
      }
    }
    return(numOfFactors)
  }else{
    return(-1)
  }
}

getFactorMatrix <- function(csvFile){
  
  datamatrix <- read.csv(file=csvFile, header = TRUE, sep = ",")
  datamatrix <- datamatrix[, -c(1,2)]
  datamatrix <- datamatrix[-c(2),]
  
  cleanedMatrix <- matrixCleaner(datamatrix)
  numOfFactors <- findFactorCount(cleanedMatrix)
  
  #Cleaning column header names of special characters
  columnCount <- ncol(cleanedMatrix)
  for(col in 1:columnCount){
    strArr <- unlist(strsplit(colnames(cleanedMatrix)[col], "[.]"))
    lengthOfString <- length(strArr)
    strArr <- strArr[-c(lengthOfString)]
    cleanedStr <- do.call(paste, c(as.list(strArr), sep = ""))
    colnames(cleanedMatrix)[col] <- cleanedStr
  }
  
  if(numOfFactors != -1){
  matrixOfLoadings <- factanal(cleanedMatrix, numOfFactors, method="MLE")
  factorsMatrix <- matrixOfLoadings$loadings
  print(factorsMatrix)
  semPaths(matrixOfLoadings, "est", minimum=minCutoff)
  }else{
    print("Insufficieant data")
  }
}

getFactorMatrix(csvFile)


