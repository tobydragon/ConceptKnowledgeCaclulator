getCleanedMatrix <- function(data){
	columnCount <- ncol(data)
	vectorsToRemove <- c()
	vectorIndex <- 1
	for(i in columnCount:1){
		#print(data[1,i])
		#print(mean(data[,i]))
		
		if(data[1, i] == mean(data[,i])){
			data <- data[,-c(i)]
			#vectorIndex <- vectorIndex + 1
			#vectorsToRemove[vectorIndex] <- i
		}
	}
	

	return(data)
}

findFactorCount <-function(data){
	numOfFactors <- 0
	#data <- as.data.frame(t(data))
	columnCount <- ncol(data)
	#print(data)
	if(columnCount > 2){
		#sets limit on how high program will go to find factors based on existing columns
		
		if(columnCount %% 2 == 0){
			upperlimit <- (columnCount/2) - 1
		}else{
			upperlimit <- (columnCount/2) - 0.5
		}
		
		#print(columnCount)

		#put p-values of factor analysis into a list
		#take the first factor number with a pvalue above 0.05
		vector = c()
		for(i in 1:upperlimit){
			vector[i] <- factanal(data, i, method='mle')$PVAL
			#print(factanal(data, i, method='mle'))
		}
		#print(vector)
		for(i in 1:upperlimit){
			if(vector[i] < 0.05){
				numOfFactors <- i + 1
			}
		}
		if(numOfFactors == 0){
			numOfFactors <- 1
		}
		return(numOfFactors)
	}
}

calculateExploratoryMatrix <- function(data){
	data <- as.data.frame(t(data))
	cleanedData <- getCleanedMatrix(data)
	if(ncol(cleanedData) > 2){
		numOfFactors <- findFactorCount(cleanedData)
	}else{
		#Not enough columns to find a factor count
		return(-1)
	}

	matrixOfLoadings <- factanal(data, factors = numOfFactors, method='mle')
	factorMatrix <- matrixOfLoadings$loadings
	return(factorMatrix)
}

calculateConfirmatoryMatrix <- function(data, modelFile){
	library(sem)
	library(semPlot)
	library(stringr)
	library(readr)
	
	model.txt <- readLines(modelFile)
	data.dhp <- specifyModel(text=model.txt)
	dataCorrelation <- cor(data)
	dataSem.dhp <- sem(data.dhp, dataCorrelation, nrow(data))
	resultMatrix <- dataSem.dhp$A
	return(resultMatrix)
}


