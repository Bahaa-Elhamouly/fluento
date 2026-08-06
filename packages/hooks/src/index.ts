import { useEffect, useState } from "react";

export function useLocalStorageState<T>(
  key: string,
  initialValue: T,
): [T, (nextValue: T) => void] {
  const [value, setValue] = useState<T>(initialValue);

  useEffect(() => {
    const storedValue = window.localStorage.getItem(key);
    if (storedValue === null) {
      return;
    }

    setValue(JSON.parse(storedValue) as T);
  }, [key]);

  function updateValue(nextValue: T) {
    window.localStorage.setItem(key, JSON.stringify(nextValue));
    setValue(nextValue);
  }

  return [value, updateValue];
}
