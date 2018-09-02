import numpy as np
import matplotlib.pyplot as plt
from include.data import get_data_set
#get_data_set is manually written function helps select train or test data from Data folder
from sklearn.linear_model import LogisticRegression
import seaborn as sns
from sklearn import metrics

train_x, new_train_y = get_data_set("train")
test_x, new_test_y = get_data_set("test")
# train_x is  collection of Vectors of size 64
# test_x is Y labels in one hot representation i.e for 4 classes: [1,0,0,0] <- representation for 1st class

train_y=np.argmax(new_train_y,axis=1)
test_y=np.argmax(new_test_y,axis=1)
#test_y is single value Y lables as Logistic Regression does not accept multi dimentional values hence now classes are 0,1,2,3 (using argmax).



logisticRegr = LogisticRegression()

logisticRegr.fit(train_x, train_y)

#selecting only the first from test set to predict the output , this row is for Eating gesture (class 0 )
print(test_x[0].reshape(1,-1))
print(logisticRegr.predict(test_x[0].reshape(1,-1)))
logisticRegr.predict(test_x[0:10])

predictions = logisticRegr.predict(test_x)


score = logisticRegr.score(test_x, test_y)
print(score)


cm = metrics.confusion_matrix(test_y, predictions)

plt.figure(figsize=(9,9))
sns.heatmap(cm, annot=True, fmt=".3f", linewidths=.5, square = True, cmap = 'Blues_r');
plt.ylabel('Actual label');
plt.xlabel('Predicted label');
all_sample_title = 'Accuracy Score: {0}'.format(score)
plt.title(all_sample_title, size = 15);
plt.savefig('LogisticRegression.png')