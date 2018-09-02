from include.data import get_data_set
#get_data_set is manually written function helps select train or test data from Data folder
import numpy as np
from sklearn.svm import LinearSVC

#Xt is  collection of Vectors of size 64
#Yt is Y labels in one hot representation i.e for 4 classes: [1,0,0,0] <- representation for 1st class
Xt, Yt = get_data_set("train")

new_y=np.argmax(Yt,axis=1)
test_x, test_y = get_data_set("test")
new_yt=np.argmax(test_y,axis=1)
#new_yt is single value Y lables as LinearSVC does not accept multi dimentional values hence now classes are 0,1,2,3 (using argmax).

#selecting only the first from test set to predict the output , this row is for Eating gesture (class 0 )
test=test_x[0,0:]

clf = LinearSVC(random_state=0)
clf = clf.fit(Xt, new_y)
prediction=clf.predict(test.reshape(-1,64))


print("Test Row of Eating Activity")
print(test)
print("*********************************")
print("Predicted Output is [Eating,Like,Rock,Victory] :")
print(prediction)
print("*********************************")
print("Accuracy for LinearSVC")
print(clf.score(test_x,new_yt)*100,"%")
