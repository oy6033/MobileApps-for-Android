
# Activity Recognition Project
In this project, we will attempt to develop a computing system that can understand human activities. There are a few components to understanding human activities: 

1. identify known activities
2. segment sequence of activities
3. identify unknown activities

We will attempt to solve all three aspects.

1. MergeFiles.py is used to merge the multiple files of collected data to single file
2. DataPreprocess.py file creates the Y lable for each record based on activity, also it created 64 size vectors for X for each activity and at the end it merges all the X and Y files together to create train_X_formatted and train_y_formatted
3. split_train_test.py is used to break the file based on the line count
4. ML Techniques Code
Code
	DecisionTree.py
	K_Nearest.py
	LinearSVC.py
	Logistic_Regression.py
	NaiveBayes.py
	SGD.py
	DeepNeuralNetwork
	1.DNN_Train.py to train the model
	2.DNN_Predict_Test_Data.py to test the model on test data
	3.DNN_Predict_Myo to test the model with Myo Band (Needs Myo Connect hence not tested)
	Include
		data.py to select train on test data based on argument
		model.py DNN model is defined here
all_backup
	Data is fetched from the Merged Folder (run DataPreprocess.py)
	Raw data is in the other folders


P.S. Absolute paths have been used in the DataPreprocess.py,DNN_Predict_Myo.py,DNN_Train.py,DNN_Predict_Test_Data.py,MergeFiles.py,split_train_test.py files
 replace : "C:/Users/Rishi/Documents/Degree/Summer 18/CSE535 Mobile Computing/Project" with  ".\Group4"
 
