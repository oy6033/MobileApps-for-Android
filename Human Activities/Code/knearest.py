from include.data import get_data_set
#get_data_set is manually written function helps select train or test data from Data folder

from sklearn.neighbors import KNeighborsClassifier

Xt, Yt = get_data_set("train")
# Xt is  collection of Vectors of size 64
# Yt is Y labels in one hot representation i.e for 4 classes: [1,0,0,0] <- representation for 1st class

test_x, test_y = get_data_set("test")
test=test_x[0,0:]

neigh = KNeighborsClassifier(n_neighbors=4)
pred = neigh.fit(Xt, Yt)

prediction = neigh.predict(test.reshape(1,64))
print("Test Row of Eating Activity")
print(test)
print("*********************************")
print("Predicted Output is [Eating,Like,Rock,Victory] :")
print(prediction)
print("*********************************")
print("Accuracy for K-Nearest")
print(neigh.score(test_x,test_y)*100,"%")

