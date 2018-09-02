import numpy as np
from include.data import get_data_set
from sklearn import tree
clf = tree.DecisionTreeClassifier()

train_x, train_y = get_data_set("train")
test_x, test_y = get_data_set("test")
# train_x is  collection of Vectors of size 64
# test_y is Y labels in one hot representation i.e for 4 classes: [1,0,0,0] <- representation for 1st class

tr_x=np.array(train_x)
tr_x.astype(int)
tr_y=np.array(train_y)
tr_y.astype(int)
ts_x=np.array(test_x)
ts_x.astype(int)

test=ts_x[1,0:]
print("Test Row of Eating Activity")
print(np.asarray(test))



clf = clf.fit(tr_x, tr_y)
prediction = clf.predict(test.reshape(1,64))
print("*********************************")
print("Predicted Output is [Eating,Like,Rock,Victory] :")
print(prediction)
print("*********************************")
print("Accuracy for Decision Tree")
print(clf.score(test_x,test_y)*100,"%")
