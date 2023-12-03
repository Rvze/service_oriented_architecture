import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { useGetUniquePricesMutation } from '../../../core/api/ticket';

export const GetUniquePricesForm: React.FC = () => {
    const { handleSubmit } = useForm();
    const [getVenueAmount] = useGetUniquePricesMutation();
    const [error, setError] = useState<number | string>('');
    const handleGetUniquePrices = async () => {
        await getVenueAmount()
            .unwrap()
            .then((res) => setError(res.toString()))
            .catch(() => setError('server error'));
    };

    return (
        <form
            className={'grid h-min w-60 grid-flow-row gap-2 rounded-2xl bg-light_purple p-5'}
            onSubmit={handleSubmit(handleGetUniquePrices)}
        >
            <button type={'submit'} className={'w-46 mt-2 h-[26px] rounded bg-purple'}>
                Get Unique Prices
            </button>
            <div className={'flex min-h-[26px] flex-wrap items-center justify-center text-red'}>{error}</div>
        </form>
    );
};
