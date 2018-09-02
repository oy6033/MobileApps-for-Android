import tensorflow as tf
import numpy as np
from sklearn.metrics import confusion_matrix
from include.model import model


from include.data import get_data_set


x, y, output, global_step, y_pred_cls = model(4)


n_test_x, n_test_y = get_data_set("test")
test_x_row=n_test_x.shape[0]
#print(test_x_row)
row=n_test_x.shape[0]-n_test_x.shape[0]%30
test_x = n_test_x[:row,:]
test_y = n_test_y[:row,:]
test_l = ["Eating","Like","Rock","Victory"]

#print("test_x" + str(np.shape(test_x)))
#print("test_y" + str(np.shape(test_y)))


saver = tf.train.Saver()
_SAVE_PATH = "C:/Users/Rishi/Documents/Degree/Summer 18/CSE535 Mobile Computing/Project/Code/data/tensorflow_sessions/myo_armband14"
sess = tf.Session()


try:
    print("Trying to restore last checkpoint ...")
    last_chk_path = tf.train.latest_checkpoint(checkpoint_dir=_SAVE_PATH)
    print(last_chk_path)
    saver.restore(sess, save_path=last_chk_path)
    print("Restored checkpoint from:", last_chk_path)
except:
    print("Failed to restore checkpoint. Initializing variables instead.")
    sess.run(tf.global_variables_initializer())


i = 0
predicted_class = np.zeros(shape=len(test_x), dtype=np.int)
while i < len(test_x):
    #print (i)

    #print(len(test_x))
    j = min(i + 30, len(test_x))
    batch_xs = test_x[i:j, :]

    #print("batch_xs"+str(np.shape(batch_xs)))
    batch_ys = test_y[i:j, :]
    #print("batch_ys" + str(np.shape(batch_ys)))
    predicted_class[i:j] = sess.run(y_pred_cls, feed_dict={x: batch_xs, y: batch_ys})
    i = j
    #print(np.shape(predicted_class))

correct = (np.argmax(test_y, axis=1) == predicted_class)
acc = correct.mean()*100
correct_numbers = correct.sum()
print("Accuracy on Test-Set: {0:.2f}% ({1} / {2})".format(acc, correct_numbers, len(test_x)))


cm = confusion_matrix(y_true=np.argmax(test_y, axis=1), y_pred=predicted_class)
for i in range(4):
    class_name = "({}) {}".format(i, test_l[i])
    print(cm[i, :], class_name)
class_numbers = [" ({0})".format(i) for i in range(4)]
print("".join(class_numbers))


sess.close()
