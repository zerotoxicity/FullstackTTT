import { Input, Text } from '@chakra-ui/react';
import { TEXT_COLOR } from '../constants';

const CustomInput = ({ setter, placeholder, required = false }) => {
  const changeHandler = e => {
    setter(e.target.value);
  };
  const textColor = TEXT_COLOR;

  return (
    <>
      <label for {...placeholder}>
        <Text as="b" {...textColor}>
          {placeholder}
        </Text>
      </label>
      <Input
        onChange={changeHandler}
        placeholder={placeholder}
        focusBorderColor="green.200"
        _placeholder={{ opacity: 1, color: '#F0FFF4' }}
        color="#F0FFF4"
        size="lg"
        isRequired={required}
      />
    </>
  );
};

export default CustomInput;
