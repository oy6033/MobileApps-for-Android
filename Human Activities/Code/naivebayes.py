
import matplotlib.pyplot as plt
import seaborn as sns
from sklearn import metrics


from include.data import get_data_set
#get_data_set is manually written function helps select train or test data from Data folder
import numpy as np

from sklearn.naive_bayes import GaussianNB

#train_x is  collection of Vectors of size 64
#new_train_y is Y labels in one hot representation i.e for 4 classes: [1,0,0,0] <- representation for 1st class

train_x, new_train_y = get_data_set("train")
test_x, new_test_y = get_data_set("test")

train_y=np.argmax(new_train_y,axis=1)
test_y=np.argmax(new_test_y,axis=1)
#test_y is single value Y lables as NaiveBayes does not accept multi dimentional values hence now classes are 0,1,2,3 (using argmax).

#selecting only the first from test set to predict the output , this row is for Eating gesture (class 0 )
test=test_x[0,0:]

gaunb = GaussianNB()
gaunb = gaunb.fit(train_x, train_y)
# create naive bayes classifier




# predict using classifier
prediction = gaunb.predict(test.reshape(1,64))

print("Test Row of Eating Activity")
print(test)
print("*********************************")
print("Predicted Output is [Eating,Like,Rock,Victory] :")
print(prediction)
print("*********************************")
print("Accuracy for Naivebayes")
print(gaunb.score(test_x,test_y)*100,"%")

cm = metrics.confusion_matrix(test_y, gaunb.predict(test_x))

plt.figure(figsize=(9,9))
sns.heatmap(cm, annot=True, fmt=".3f", linewidths=.5, square = True, cmap = 'Blues_r');
plt.ylabel('Actual label');
plt.xlabel('Predicted label');
all_sample_title = 'Accuracy Score: {0}'.format(gaunb.score(test_x,test_y))
plt.title(all_sample_title, size = 15);
plt.savefig('nb.png')
