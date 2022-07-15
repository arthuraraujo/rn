/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow strict-local
 */

import React from 'react';
import { NativeEventEmitter, NativeModules, Button } from 'react-native';

const { ActivationModule } = NativeModules;

const NewModuleButton = () => {
  const onSubmit = async () => {
    // created listener
    const onStatusEvent = (event) => {
      console.log(`event listener: ${event.status}`);
    };

    const eventEmitter = new NativeEventEmitter(ActivationModule);
    const subscription = eventEmitter.addListener(
      'onStatusEvent',
      onStatusEvent
    );
    // subscription.remove(); unsubscribe your listeners once they’re not needed anymore or you’ll end up leaking memory. Don’t forget that!

    // pass sandbox identification (sandbox user, cloud server url)
    ActivationModule.setAccountIdentifier(
      'sybrandreinders-vort',
      '.sdb.tid.onespan.cloud'
    );

    // start activation
    ActivationModule.startActivation('felipe101', 'Test1234');
  };

  return (
    <Button
      title="Click to invoke your native module!"
      color="#841584"
      onPress={onSubmit}
    />
  );
};

export default NewModuleButton;

// import * as React from 'react';

// import { StyleSheet, View, Text } from 'react-native';
// import { multiply } from 'react-native-onespan-orchestration';

// export default function App() {
//   const [result, setResult] = React.useState<number | undefined>();

//   React.useEffect(() => {
//     multiply(3, 7).then(setResult);
//   }, []);

//   return (
//     <View style={styles.container}>
//       <Text>Result: {result}</Text>
//     </View>
//   );
// }

// const styles = StyleSheet.create({
//   container: {
//     flex: 1,
//     alignItems: 'center',
//     justifyContent: 'center',
//   },
//   box: {
//     width: 60,
//     height: 60,
//     marginVertical: 20,
//   },
// });
