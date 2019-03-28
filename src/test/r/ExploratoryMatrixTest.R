#TEST
calculateExploratoryMatrixTest <- function(){
	
	#Hardcoded matrix test data
	
	#data <- matrix(c(.3,.43,.48,.2, .2,.2,.2,.2, .7,.9,.83,.6, .34,.27,.11,.10, .45,.43,.3,.20, .21,.25,.16,.22, .51,.7,.6,.8, .46,.32,.5,.8), nrow=4, ncol=8)
	#data <- as.data.frame(t(data))
	
	#CSV test data
	
	data = read.csv("/Users/bleblanc2/Documents/AthleticsDataOnR.csv", sep=",", header = FALSE)
	#print(data)
	data <- (t(data))
	#data <- spss.get("/Users/bleblanc2/Documents/AthleticsData.sav")
	#data <- t(data)
	print(data)
	 
	
	factorMatrix <- calculateExploratoryMatrix(data)
	print(factorMatrix)
#	if(factorMatrix[1][1] > 0){
#		print('calculateExploratoryMatrix: PASS')
#		print(factorMatrix)
#	}else{
#		print('calculateExploratoryMatrix: FAIL')
#		print(factorMatrix)
#	}
}


#TEST
findFactorCountTest <- function(){
	#data <- matrix(c(.3,.43,.48,.2,.52,.39, .2,.2,.2,.2,.2,.2, .7,.9,.83,.6,.72,.78, .34,.27,.11,.10,.4,.7, .45,.43,.3,.20,.56,.3, .21,.25,.16,.22,.3,.34, .51,.7,.6,.8,.76,.87, .46,.32,.5,.8,.13,.19), nrow=6, ncol=8)
	
	data <- matrix(c(.3,.43,.48,.2, .2,.2,.2,.2, .7,.9,.83,.6, .34,.27,.11,.10, .45,.43,.3,.20, .21,.25,.16,.22, .51,.7,.6,.8, .46,.32,.5,.8), nrow=4, ncol=8)
	
	#data <- matrix(c(.3,.43,.48,.2,.52,.39, .2,.2,.2,.2,.2,.2, .7,.9,.83,.6,.72,.78, .34,.27,.11,.10,.4,.7, .45,.43,.3,.20,.56,.3),nrow=6, ncol=5)
	
	data <- as.data.frame(t(data))
	cleanedData <- getCleanedMatrix(data)
	if(ncol(cleanedData) > 2){
		numOfFactors <- findFactorCount(cleanedData)
	}else{
		#Not enough columns to find factor count
	}
	
	if(numOfFactors == 1){
		print('findFactorCount: PASS')
	}else{
		print('findFactorCount: FAIL')
	}
}


#TEST
getCleanedMatrixTest <- function(){
	data <- matrix(
	c(1,4,5,2, 2,2,2,2, 7,9,3,6, 11,10,4,7, 3,3,3,3, 21,25,16,9),
	nrow=4, ncol=6)

	cleanedData <- getCleanedMatrix(data)
	if(ncol(cleanedData) == 4){
		print('getColumnCount: PASS')
	}else{
		print('getColumnCount: FAIL')
	}
}


#TEST
calculateConfirmatoryMatrixTest <- function(){
	data = read.csv("/Users/bleblanc2/Documents/AthleticsDataOnTecMap.csv", sep=",", header = TRUE)
	#data <- as.data.frame(data)
	modelFile <- '/Users/bleblanc2/IdeaProjects/tecmap/src/test/resources/model/model.txt'
	resultMatrix = calculateConfirmatoryMatrix(data, modelFile)


	print(resultMatrix)
	
}

source("/Users/bleblanc2/IdeaProjects/tecmap/src/main/r/ExploratoryMatrix.R")

#getCleanedMatrixTest()
#findFactorCountTest()
#calculateExploratoryMatrixTest()
calculateConfirmatoryMatrixTest()